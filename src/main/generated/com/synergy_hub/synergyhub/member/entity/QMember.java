package com.synergy_hub.synergyhub.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 177814867L;

    public static final QMember member = new QMember("member1");

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.synergy_hub.synergyhub.team.entity.MemberTeam, com.synergy_hub.synergyhub.team.entity.QMemberTeam> memberTeams = this.<com.synergy_hub.synergyhub.team.entity.MemberTeam, com.synergy_hub.synergyhub.team.entity.QMemberTeam>createList("memberTeams", com.synergy_hub.synergyhub.team.entity.MemberTeam.class, com.synergy_hub.synergyhub.team.entity.QMemberTeam.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

