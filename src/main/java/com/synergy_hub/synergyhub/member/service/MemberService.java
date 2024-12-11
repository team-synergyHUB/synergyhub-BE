package com.synergy_hub.synergyhub.member.service;

import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.image.S3ImageService;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.dto.MemberLoginRequest;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
import com.synergy_hub.synergyhub.member.dto.MemberUpdateRequest;
import com.synergy_hub.synergyhub.member.dto.TeamMemberResponseDto;
import com.synergy_hub.synergyhub.member.entity.LoginType;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberRole;
import com.synergy_hub.synergyhub.member.exception.EmailAlreadyExistException;
import com.synergy_hub.synergyhub.member.exception.MemberNotFoundException;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.team.service.MemberTeamService;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberTeamService memberTeamService;
    private final S3ImageService s3ImageService;


    public Long save(MemberAddRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = Member.createMember(request.getNickname(), request.getEmail(),
            passwordEncoder.encode(request.getPassword()));
        member.changeRole(MemberRole.USER);
        member.changeLoginType(LoginType.COMMON);

        return memberRepository.save(member).getId();
    }


    //모든 회원 조회
    public List<MemberResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAllByDeletedAtIsNull();

        if(members.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return members.stream()
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }

    //특정 팀에 속한 회원 조회
    public List<MemberResponseDto> findAllByTeam(Long teamId) {
        List<Member> membersByTeam = memberRepository.findMembersByTeam(teamId);

        if(membersByTeam.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return membersByTeam.stream()
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }

    //특정 팀에 속한 회원 조회(페이징)
    public Page<TeamMemberResponseDto> findAllByTeamPaging(Long teamId, Pageable pageable) {
        Page<TeamMemberResponseDto> TeamMembers = memberRepository.findMembersByTeam(teamId,
            pageable);

        if(TeamMembers.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return TeamMembers;
    }

    //이메일로 회원 조회
    public MemberResponseDto findByEmail(String email) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(email)
            .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        return new MemberResponseDto(member);
    }

    //id로 회원 조회
    public MemberResponseDto findById(Long id) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        return new MemberResponseDto(member);
    }

    //회원 프로필 닉네임 수정
    @Transactional
    public Long updateMemberInfo(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        member.updateMyInfo(request.getNickname());

        return member.getId();
    }

    //회원 프로필 이미지 수정
    @Transactional
    public Long updateMemberProfileImage(Long id, MultipartFile profileImage) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (member.getProfileImageUrl() != null) {
            s3ImageService.deleteImageFromS3(member.getProfileImageUrl());
        }

        String uploadedImageUrl = s3ImageService.upload(profileImage);
        member.updateProfileImage(uploadedImageUrl);

        return member.getId();
    }


    //회원 탈퇴(soft delete)
    public void deleteMember(Long id) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));

        member.deleteAccount();
    }

}
