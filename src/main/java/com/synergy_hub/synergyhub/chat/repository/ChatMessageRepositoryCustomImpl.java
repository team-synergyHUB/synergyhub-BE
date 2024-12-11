package com.synergy_hub.synergyhub.chat.repository;

import static com.synergy_hub.synergyhub.chat.entity.QChatMessage.*;
import static com.synergy_hub.synergyhub.chat.entity.QChatRoom.*;
import static com.synergy_hub.synergyhub.member.entity.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.dto.QChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatMessage.MessageType;
import com.synergy_hub.synergyhub.chat.entity.QChatRoom;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ChatMessageRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean hasJoined(Long memberId, Long chatRoomId) {

        return queryFactory
            .selectFrom(chatMessage)
            .join(chatMessage.member, member).fetchJoin()
            .join(chatMessage.chatRoom, chatRoom).fetchJoin()
            .where(member.id.eq(memberId), chatRoom.roomId.eq(chatRoomId))
            .fetchFirst() != null;
    }

    @Override
    public List<ChatMessageResponseDto> findHistoryByChatRoomId(Long memberId, Long chatRoomId) {

        //채팅방 입장한 시간
        LocalDateTime enterTime = queryFactory
            .select(chatMessage.createdAt)
            .from(chatMessage)
            .where(chatMessage.chatRoom.roomId.eq(chatRoomId), chatMessage.member.id.eq(memberId),
                chatMessage.type.eq(MessageType.ENTER))
            .orderBy(chatMessage.createdAt.asc())
            .fetchFirst();

        if (enterTime == null) {
            throw new IllegalStateException("No Enter time");
        }

        return queryFactory
            .select(new QChatMessageResponseDto(
                chatMessage.messageId,
                chatRoom.roomId,
                member.nickname,
                member.email,
                member.profileImageUrl,
                chatMessage.message,
                chatMessage.type,
                chatRoom.createdAt
            ))
            .from(chatMessage)
            .join(chatMessage.chatRoom, chatRoom)
            .join(chatMessage.member, member)
            .where(chatRoom.roomId.eq(chatRoomId), chatMessage.createdAt.goe(enterTime))
            .fetch();
    }

    @Override
    public boolean hasEnterType(Long memberId, Long chatRoomId) {
        return queryFactory
            .select(chatMessage.type)
            .from(chatMessage)
            .where(chatMessage.member.id.eq(memberId), chatMessage.chatRoom.roomId.eq(chatRoomId))
            .fetchFirst() != null;
    }
}
