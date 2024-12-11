package com.synergy_hub.synergyhub.member.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.synergy_hub.synergyhub.member.dto.QTeamMemberResponseDto is a Querydsl Projection type for TeamMemberResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QTeamMemberResponseDto extends ConstructorExpression<TeamMemberResponseDto> {

    private static final long serialVersionUID = 99591238L;

    public QTeamMemberResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<java.time.LocalDateTime> deletedAt) {
        super(TeamMemberResponseDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDateTime.class}, id, nickname, email, deletedAt);
    }

}

