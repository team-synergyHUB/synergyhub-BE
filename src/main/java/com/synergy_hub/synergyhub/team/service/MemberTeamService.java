package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
//@Transactional
public class MemberTeamService {
    private final MemberTeamRepository memberTeamRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public MemberTeamService(MemberTeamRepository memberTeamRepository, TeamRepository teamRepository,
                             MemberRepository memberRepository) {
        this.memberTeamRepository = memberTeamRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void addMemberToTeam(String inviteCode, Long memberId) {
        // 1. 초대 코드로 팀 조회
        Team team = teamRepository.findByInviteCodeAndIsDeletedFalse(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("초대 코드에 해당하는 팀이 없습니다."));

        // 2. 이미 소속된 회원인지 확인
        boolean isAlreadyMember = memberTeamRepository.existsByTeamAndMemberId(team, memberId);
        if (isAlreadyMember) {
            throw new IllegalStateException("이미 해당 팀에 소속된 회원입니다.");
        }

        // 3. 소속 추가
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        MemberTeam memberTeam = MemberTeam.builder()
                .team(team)
                .member(member)
                .color("#00000") // 또는 로직에 따라 값 설정
                .build();

        memberTeamRepository.save(memberTeam);
    }

    @Transactional
    public void removeMemberFromTeam(Long teamId, Long memberId) {
        // 1. 팀 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 멤버가 해당 팀에 속해 있는지 확인
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_TEAM_NOT_FOUND));

        // 3. 팀에서 멤버 제거
        memberTeamRepository.delete(memberTeam);

        // 4. 팀에 남은 멤버가 있는지 확인
        boolean hasRemainingMembers = memberTeamRepository.existsByTeam(team);
        if (!hasRemainingMembers) {
            // 멤버가 없다면 팀을 삭제하거나 isDeleted 상태로 변경
            team.markAsDeleted(); // isDeleted 필드를 true로 설정하는 메서드
            teamRepository.save(team);
        }
    }

    // 특정 팀의 멤버 목록 조회
    public List<Member> getMembersOfTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        return memberTeamRepository.findAllByTeam(team)
                .stream()
                .map(MemberTeam::getMember) // MemberTeam 엔티티의 getMemberId() 메서드를 호출하여 멤버 ID만 추출
                .toList();
    }

    // 사용자가 속한 MemberTeam 조회
    public List<MemberTeam> getMemberTeamsByMember(Long memberId) {
        return memberTeamRepository.findAllByMemberId(memberId);
    }







    //색상 변경
    public void updateColor(Long memberId, Long teamId, String newColor){
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(memberId, teamId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_TEAM_NOT_FOUND));

        memberTeam.updateColor(newColor);
        memberTeamRepository.save(memberTeam);
    }

    // 팀 색상 조회
    public String getTeamColor(Long memberId, Long teamId) {
        return memberTeamRepository.findByMemberIdAndTeamId(memberId, teamId)
            .map(MemberTeam::getColor)
            .orElse("#00000");
    }

    // 사용자가 소속된 모든 팀 색상조회
    public Map<Long, String> getAllTeamColor(Long memberId){
        return memberTeamRepository.findAllByMemberId(memberId).stream()
            .collect(Collectors.toMap(
                memberTeam -> memberTeam.getTeam().getId(),
                MemberTeam::getColor
                ));
    }

    // 특정 팀의 멤버 여부 확인 (Optional 활용)
    public Optional<MemberTeam> findMemberTeam(Long memberId, Long teamId) {
        return memberTeamRepository.findByMemberIdAndTeamId(memberId, teamId);
    }

    // 팀 멤버 권한 검증 (Optional 활용)
    public void validateMemberOfTeam(Long memberId, Long teamId) {
        findMemberTeam(memberId, teamId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_TEAM_NOT_FOUND));
    }







}