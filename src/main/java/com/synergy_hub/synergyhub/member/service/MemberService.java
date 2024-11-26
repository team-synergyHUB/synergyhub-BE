package com.synergy_hub.synergyhub.member.service;

import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberTeamService memberTeamService;

    @Transactional
    public Long save(MemberAddRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String password = passwordEncoder.encode(request.getPassword()); //비밀번호 암호화
        Member member = Member.createMember(request.getNickname(), request.getEmail(), password);
        member.changeRole(MemberRole.USER);
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

}
