package com.synergy_hub.synergyhub.comment.mapper;

import com.synergy_hub.synergyhub.comment.dto.CommentRequestDto;
import com.synergy_hub.synergyhub.comment.dto.CommentResponseDto;
import com.synergy_hub.synergyhub.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    // CommentRequestDto -> Comment 엔티티 변환
    @Mapping(source = "noticeId", target = "noticeId")     // 공지사항 ID
    @Mapping(source = "memberId", target = "memberId")     // 작성자 ID
    @Mapping(source = "teamId", target = "teamId")         // 팀 ID
    @Mapping(source = "content", target = "content")       // 댓글 내용
    @Mapping(target = "isDeleted", constant = "false")     // 기본값 설정
    Comment toEntity(CommentRequestDto dto);

    // Comment 엔티티 -> CommentResponseDto 변환
    @Mapping(source = "commentId", target = "commentId")   // 댓글 ID
    @Mapping(source = "noticeId", target = "noticeId")     // 공지사항 ID
    @Mapping(source = "memberId", target = "memberId")     // 작성자 ID
    @Mapping(source = "teamId", target = "teamId")         // 팀 ID
    @Mapping(source = "content", target = "content")       // 댓글 내용
    @Mapping(source = "isDeleted", target = "isDeleted")   // 삭제 여부
    @Mapping(source = "createdAt", target = "createdAt")   // 생성 시간
    @Mapping(source = "updatedAt", target = "updatedAt")   // 수정 시간
    CommentResponseDto toDto(Comment comment);
}
