package com.synergy_hub.synergyhub.comment.repository;


import com.synergy_hub.synergyhub.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 공지사항의 삭제되지 않은 댓글 조회
    List<Comment> findByNotice_IdAndIsDeletedFalse(Long noticeId);

    // 특정 팀에 속한 댓글 조회
    List<Comment> findByTeamIdAndIsDeletedFalse(Long teamId);
}

