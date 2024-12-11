package com.synergy_hub.synergyhub.chat.mapper;

import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.dto.ChatResponseDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.entity.Chat;
import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.team.entity.Team;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-03T17:28:35+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatMessageResponseDto toChatMessageResponseDto(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }

        ChatMessageResponseDto.ChatMessageResponseDtoBuilder chatMessageResponseDto = ChatMessageResponseDto.builder();

        chatMessageResponseDto.messageId( chatMessage.getMessageId() );
        chatMessageResponseDto.roomId( chatMessageChatRoomRoomId( chatMessage ) );
        chatMessageResponseDto.nickname( chatMessageMemberNickname( chatMessage ) );
        chatMessageResponseDto.message( chatMessage.getMessage() );
        chatMessageResponseDto.type( chatMessage.getType() );
        chatMessageResponseDto.createdAt( chatMessage.getCreatedAt() );
        chatMessageResponseDto.deletedAt( chatMessage.getDeletedAt() );

        return chatMessageResponseDto.build();
    }

    @Override
    public ChatRoomResponseDto toChatRoomResponseDto(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        ChatRoomResponseDto.ChatRoomResponseDtoBuilder chatRoomResponseDto = ChatRoomResponseDto.builder();

        chatRoomResponseDto.roomId( chatRoom.getRoomId() );
        chatRoomResponseDto.teamName( chatRoomTeamName( chatRoom ) );
        chatRoomResponseDto.createdAt( chatRoom.getCreatedAt() );
        chatRoomResponseDto.deletedAt( chatRoom.getDeletedAt() );

        return chatRoomResponseDto.build();
    }

    @Override
    public ChatResponseDto toChatResponseDto(Chat chat) {
        if ( chat == null ) {
            return null;
        }

        ChatResponseDto.ChatResponseDtoBuilder chatResponseDto = ChatResponseDto.builder();

        chatResponseDto.roomId( chatChatRoomRoomId( chat ) );
        chatResponseDto.memberName( chatMemberNickname( chat ) );
        chatResponseDto.createdAt( chat.getCreatedAt() );
        chatResponseDto.id( chat.getId() );

        return chatResponseDto.build();
    }

    private Long chatMessageChatRoomRoomId(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }
        ChatRoom chatRoom = chatMessage.getChatRoom();
        if ( chatRoom == null ) {
            return null;
        }
        Long roomId = chatRoom.getRoomId();
        if ( roomId == null ) {
            return null;
        }
        return roomId;
    }

    private String chatMessageMemberNickname(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }
        Member member = chatMessage.getMember();
        if ( member == null ) {
            return null;
        }
        String nickname = member.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }

    private String chatRoomTeamName(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }
        Team team = chatRoom.getTeam();
        if ( team == null ) {
            return null;
        }
        String name = team.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long chatChatRoomRoomId(Chat chat) {
        if ( chat == null ) {
            return null;
        }
        ChatRoom chatRoom = chat.getChatRoom();
        if ( chatRoom == null ) {
            return null;
        }
        Long roomId = chatRoom.getRoomId();
        if ( roomId == null ) {
            return null;
        }
        return roomId;
    }

    private String chatMemberNickname(Chat chat) {
        if ( chat == null ) {
            return null;
        }
        Member member = chat.getMember();
        if ( member == null ) {
            return null;
        }
        String nickname = member.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }
}
