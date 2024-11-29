package com.synergy_hub.synergyhub.member.repository;

import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.query.MemberRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

    //-------------------------




    @Query("select m from Member m join m.memberTeams mt"
        + " where mt.team.id = :teamId and m.deletedAt is null")
    List<Member> findMembersByTeam(Long teamId);


}
