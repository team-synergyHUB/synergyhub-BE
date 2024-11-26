package com.synergy_hub.synergyhub.member.repository;

import com.synergy_hub.synergyhub.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);


    //-------------------------

    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    List<Member> findAllByDeletedAtIsNull();

    boolean existsByEmail(String email);

    @Query("select m from Member m join m.memberTeams mt"
        + " where mt.team.id = :teamId and m.deletedAt is null")
    List<Member> findMembersByTeam(Long teamId);


}
