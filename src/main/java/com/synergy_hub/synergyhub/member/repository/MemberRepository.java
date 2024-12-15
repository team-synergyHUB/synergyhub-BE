package com.synergy_hub.synergyhub.member.repository;

import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.query.MemberRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

    //-------------------------


    @Query("select m from Member m join m.memberTeams mt"
        + " where mt.team.id = :teamId and m.deletedAt is null")
    List<Member> findMembersByTeam(Long teamId);

    // 멤버가 속한 팀 리스트를 확인하는 메소드
    @Query("SELECT mt.team.id FROM MemberTeam mt WHERE mt.member.id = :memberId")
    List<Long> findTeamIdsByMemberId(@Param("memberId") Long memberId);

    // memberId를 통해 memberNickname을 가져오는 메소드 추가
    @Query("SELECT m.nickname FROM Member m WHERE m.id = :memberId AND m.deletedAt IS NULL")
    Optional<String> findNicknameByMemberId(@Param("memberId") Long memberId);
}
