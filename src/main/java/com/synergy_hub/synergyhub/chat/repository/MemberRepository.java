package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
}