package com.synergy_hub.synergyhub.notice.repository;

import com.synergy_hub.synergyhub.notice.entity.Notice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    //팀 ID와 소프트 삭제 여부로 공지사항 조회
    List<Notice> findByTeamIdAndDeletedAtIsNull(Long teamId);

    //ID로 특정 공지사항 조회(삭제되지 않은 것만)
    Optional<Notice> findByIdAndDeletedAtIsNull(Long id);

    //삭제되지 않은 모든 공지사항 조회
    List<Notice> findAllByDeletedAtIsNull();

}
