package com.synergy_hub.synergyhub.comment.mapper;

import com.synergy_hub.synergyhub.comment.dto.CommentRequestDto;
import com.synergy_hub.synergyhub.comment.dto.CommentResponseDto;
import com.synergy_hub.synergyhub.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    // CommentRequestDto -> Comment 엔티티 변환
    @Mappings({
            @Mapping(source = "noticeId", target = "noticeId"), // 공지사항 ID
            @Mapping(source = "content", target = "content"),   // 댓글 내용
            @Mapping(target = "isDeleted", constant = "false"), // 기본값 설정
            @Mapping(target = "memberId", ignore = true),       // memberId는 서비스에서 설정
            @Mapping(target = "nickname", ignore = true),       // 작성자 nickname
            @Mapping(target = "teamId", ignore = true),         // teamId는 서비스에서 설정
            @Mapping(target = "commentId", ignore = true),      // 자동 생성
            @Mapping(target = "createdAt", ignore = true),      // 자동 설정
            @Mapping(target = "updatedAt", ignore = true)       // 자동 설정
    })
    Comment toEntity(CommentRequestDto dto);

    // Comment 엔티티 -> CommentResponseDto 변환
    @Mappings({
            @Mapping(source = "commentId", target = "commentId"), // 댓글 ID
            @Mapping(source = "noticeId", target = "noticeId"),   // 공지사항 ID
            @Mapping(source = "memberId", target = "memberId"),   // 작성자 ID
            @Mapping(source = "nickname", target = "nickname"),   // 작성자 nickname
            @Mapping(source = "teamId", target = "teamId"),       // 팀 ID
            @Mapping(source = "content", target = "content"),     // 댓글 내용
            @Mapping(source = "isDeleted", target = "isDeleted"), // 삭제 여부
            @Mapping(source = "createdAt", target = "createdAt"), // 생성 시간
            @Mapping(source = "updatedAt", target = "updatedAt")  // 수정 시간
    })
    CommentResponseDto toDto(Comment comment);
}
