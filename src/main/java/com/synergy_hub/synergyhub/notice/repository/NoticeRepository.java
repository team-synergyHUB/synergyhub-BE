package com.synergy_hub.synergyhub.notice.repository;

import com.synergy_hub.synergyhub.notice.entity.Notice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 특정 팀 ID로 공지사항 조회 (삭제되지 않은 것만)
    List<Notice> findByTeamIdAndDeletedAtIsNull(Long teamId);

    // 특정 공지사항 ID로 조회 (삭제되지 않은 것만)
    Optional<Notice> findByIdAndDeletedAtIsNull(Long id);

    // 삭제되지 않은 모든 공지사항 조회
    List<Notice> findAllByDeletedAtIsNull();

    // 특정 멤버가 속한 팀의 공지사항 조회
    List<Notice> findByTeamIdAndDeletedAtIsNullAndMemberId(Long teamId, Long memberId);

    // 공지사항 제목 및 내용 수정
    @Modifying
    @Query("UPDATE Notice n SET n.title = :title, n.content = :content, n.imageUrl = :imageUrl, n.updatedAt = CURRENT_TIMESTAMP WHERE n.id = :id AND n.deletedAt IS NULL")
    int updateNotice(
            @Param("id") Long id,
            @Param("title") String title,
            @Param("content") String content,
            @Param("imageUrl") String imageUrl
    );

    // 공지사항 소프트 딜리트 처리
    @Modifying
    @Query("UPDATE Notice n SET n.deletedAt = CURRENT_TIMESTAMP WHERE n.id = :id AND n.deletedAt IS NULL")
    void softDeleteNotice(@Param("id") Long id);
}
