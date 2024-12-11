package com.synergy_hub.synergyhub.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeam is a Querydsl query type for Team
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeam extends EntityPathBase<Team> {

    private static final long serialVersionUID = -1848356071L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeam team = new QTeam("team");

    public final com.synergy_hub.synergyhub.chat.entity.QChatRoom chatRoom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inviteCode = createString("inviteCode");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<Label, QLabel> labels = this.<Label, QLabel>createList("labels", Label.class, QLabel.class, PathInits.DIRECT2);

    public final ListPath<MemberTeam, QMemberTeam> memberTeams = this.<MemberTeam, QMemberTeam>createList("memberTeams", MemberTeam.class, QMemberTeam.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public QTeam(String variable) {
        this(Team.class, forVariable(variable), INITS);
    }

    public QTeam(Path<? extends Team> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeam(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeam(PathMetadata metadata, PathInits inits) {
        this(Team.class, metadata, inits);
    }

    public QTeam(Class<? extends Team> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.synergy_hub.synergyhub.chat.entity.QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
    }

}

