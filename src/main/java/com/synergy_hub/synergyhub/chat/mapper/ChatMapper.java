package com.synergy_hub.synergyhub.chat.mapper;

import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.dto.ChatResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    // ChatMessage -> ChatMessageResponseDto
    @Mapping(source = "messageId", target = "messageId") // 메시지 ID
    @Mapping(source = "chatRoom.roomId", target = "roomId") // 채팅방 ID
    @Mapping(source = "chatRoom.roomName", target = "roomName") // 채팅방 이름
    @Mapping(source = "member.nickname", target = "senderNickname") // 보낸 사람 닉네임
    @Mapping(source = "message", target = "message") // 메시지 내용
    @Mapping(source = "type", target = "type") // 메시지 타입
    @Mapping(source = "createdAt", target = "createdAt") // 생성 시간
    @Mapping(source = "deletedAt", target = "deletedAt") // 삭제된 시간
    ChatMessageResponseDto toChatMessageResponseDto(ChatMessage chatMessage);

    // ChatRoom -> ChatRoomResponseDto
    @Mapping(source = "roomId", target = "roomId") // 채팅방 ID
    @Mapping(source = "roomName", target = "roomName") // 채팅방 이름
    @Mapping(source = "roomState", target = "roomState") // 채팅방 상태
    @Mapping(source = "team.name", target = "teamName") // 연결된 팀 이름
    @Mapping(source = "createdAt", target = "createdAt") // 생성 시간
    @Mapping(source = "deletedAt", target = "deletedAt") // 삭제 여부
    ChatRoomResponseDto toChatRoomResponseDto(ChatRoom chatRoom);

    // Chat -> ChatResponseDto
    @Mapping(source = "chatRoom.roomId", target = "roomId") // 채팅방 ID
    @Mapping(source = "chatRoom.roomName", target = "roomName") // 채팅방 이름
    @Mapping(source = "member.nickname", target = "memberName") // 회원 닉네임
    @Mapping(source = "createdAt", target = "createdAt") // 참여 시간
    ChatResponseDto toChatResponseDto(Chat chat);
}
