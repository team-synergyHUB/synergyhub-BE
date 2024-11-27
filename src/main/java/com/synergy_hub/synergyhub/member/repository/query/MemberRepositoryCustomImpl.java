package com.synergy_hub.synergyhub.member.repository.query;

import static com.synergy_hub.synergyhub.member.entity.QMember.*;
import static com.synergy_hub.synergyhub.team.entity.QMemberTeam.*;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy_hub.synergyhub.member.dto.QTeamMemberResponseDto;
import com.synergy_hub.synergyhub.member.dto.TeamMemberResponseDto;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.team.entity.QMemberTeam;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Member> findByIdAndDeletedAtIsNull(Long id) {

        Member result = queryFactory
            .selectFrom(member)
            .where(member.id.eq(id), member.deletedAt.isNull())
            .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Member> findByEmailAndDeletedAtIsNull(String email) {

        Member result = queryFactory
            .selectFrom(member)
            .where(member.email.eq(email), member.deletedAt.isNull())
            .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Member> findAllByDeletedAtIsNull() {

         return queryFactory
            .selectFrom(member)
            .where(member.deletedAt.isNull())
            .fetch();
    }

    @Override
    public Page<TeamMemberResponseDto> findMembersByTeam(Long teamId, Pageable pageable) {

        List<TeamMemberResponseDto> result = queryFactory
            .select(new QTeamMemberResponseDto(
                member.id,
                member.nickname,
                member.email,
                member.deletedAt
            ))
            .from(memberTeam)
            .join(memberTeam.member, member)
            .where(
                memberTeam.team.id.eq(teamId),
                member.deletedAt.isNull())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(memberTeam.count())
            .from(memberTeam)
            .where(
                memberTeam.team.id.eq(teamId),
                member.deletedAt.isNull()
            );

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);

    }


}
