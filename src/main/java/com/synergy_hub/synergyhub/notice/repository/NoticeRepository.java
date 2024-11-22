package com.synergy_hub.synergyhub.notice.repository;

import com.synergy_hub.synergyhub.notice.entity.Notice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByTeamIdAndDeletedAtIsNull(Long teamId);

    Optional<Notice> findByIdAndDeletedAtIsNull(Long id);

    List<Notice> findAllByDeletedAtIsNull();

}
