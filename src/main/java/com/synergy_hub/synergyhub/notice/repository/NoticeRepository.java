package com.synergy_hub.synergyhub.notice.repository;

import com.synergy_hub.synergyhub.notice.entity.Notice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    //팀 ID와 소프트 삭제 여부로 공지사항 조회
    Page<Notice> findByTeamIdAndDeletedAtIsNull(Long teamId, Pageable pageable);

    //ID로 특정 공지사항 조회(삭제되지 않은 것만)
    Optional<Notice> findByIdAndDeletedAtIsNull(Long id);

    //삭제되지 않은 모든 공지사항 조회
    List<Notice> findAllByDeletedAtIsNull();

    // 멤버가 속한 팀의 공지사항 조회
    List<Notice> findByTeamIdAndDeletedAtIsNullAndMemberId(Long teamId, Long memberId);
}

