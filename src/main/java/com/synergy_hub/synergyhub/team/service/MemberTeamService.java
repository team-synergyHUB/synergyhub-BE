package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
//@Transactional
public class MemberTeamService {
    private final MemberTeamRepository memberTeamRepository;
    private final TeamRepository teamRepository;
    //private final MemberRepository memberRepository;

    public MemberTeamService(MemberTeamRepository memberTeamRepository, TeamRepository teamRepository) {
        this.memberTeamRepository = memberTeamRepository;
        this.teamRepository = teamRepository;
    }

    // 팀에 멤버 추가
    public void addMemberToTeam(Long teamId, Member member) {
        // 팀 존재 여부 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 2. 멤버 존재 여부 확인 -> 필요 없을 듯
//        boolean memberExists = memberRepository.existsById(memberId);
//        if (!memberExists) {
//            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
//        }

        MemberTeam memberTeam = new MemberTeam(member, team);
        memberTeamRepository.save(memberTeam);
    }
//
//    // 팀에서 멤버 제거
//    public void removeMemberFromTeam(Long teamId, Long memberId) {
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
//
//        // 2. 멤버 존재 여부 확인
//        // MemberRepository 생기면 주석 풀기
////        boolean memberExists = memberRepository.existsById(memberId);
////        if (!memberExists) {
////            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
////        }
//
//        memberTeamRepository.deleteByMemberIdAndTeam(memberId, team);
//    }

    // 특정 팀의 멤버 목록 조회
    public List<Member> getMembersOfTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        return memberTeamRepository.findAllByTeam(team)
                .stream()
                .map(MemberTeam::getMember) // MemberTeam 엔티티의 getMemberId() 메서드를 호출하여 멤버 ID만 추출
                .toList();
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




}