package com.synergy_hub.synergyhub.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberTeam is a Querydsl query type for MemberTeam
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberTeam extends EntityPathBase<MemberTeam> {

    private static final long serialVersionUID = 1341454291L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberTeam memberTeam = new QMemberTeam("memberTeam");

    public final StringPath color = createString("color");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.synergy_hub.synergyhub.member.entity.QMember member;

    public final QTeam team;

    public QMemberTeam(String variable) {
        this(MemberTeam.class, forVariable(variable), INITS);
    }

    public QMemberTeam(Path<? extends MemberTeam> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberTeam(PathMetadata metadata, PathInits inits) {
        this(MemberTeam.class, metadata, inits);
    }

    public QMemberTeam(Class<? extends MemberTeam> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.synergy_hub.synergyhub.member.entity.QMember(forProperty("member")) : null;
        this.team = inits.isInitialized("team") ? new QTeam(forProperty("team"), inits.get("team")) : null;
    }

}

