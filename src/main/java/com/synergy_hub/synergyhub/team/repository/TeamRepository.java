package com.synergy_hub.synergyhub.team.repository;

import com.synergy_hub.synergyhub.team.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    // 초대 코드를 통해 특정 팀 조회
    Optional<Team> findByInviteCode(String inviteCode);

    // 삭제되지 않은 모든 팀 조회
//    List<Team> findAllByIsDeleted(Boolean isDeleted);

    Page<Team> findAllByIsDeleted(boolean isDeleted, Pageable pageable);

    List<Team> findAllByIsDeleted(boolean isDeleted);

    boolean existsByInviteCode(String inviteCode); // 초대 코드 중복 검사

}
