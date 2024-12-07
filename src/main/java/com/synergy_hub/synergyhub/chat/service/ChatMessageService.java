package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.dto.ChatMessageRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.mapper.ChatMapper;
import com.synergy_hub.synergyhub.chat.repository.ChatMessageRepository;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMapper chatMessageMapper;

    // 메시지 전송
    public ChatMessageResponseDto sendMessage(Long chatRoomId, ChatMessageRequestDto requestDto, String email) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.builder()
            .chatRoom(chatRoom)
            .member(member)
            .type(requestDto.getType())
            .message(requestDto.getMessage()) // ChatMessageContent에서 텍스트 추출
            .createdAt(LocalDateTime.now())
            .build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        return chatMessageMapper.toChatMessageResponseDto(savedMessage);
    }

    // 메시지 삭제
    public Long deleteMessage(Long messageId,  String email) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!chatMessage.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        chatMessage.delete(); // deletedAt 설정
        chatMessageRepository.save(chatMessage);
        return messageId;
    }

    // 채팅방 별 메시지 조회
    public List<ChatMessageResponseDto> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom_RoomId(chatRoomId);
        return chatMessages.stream()
                .map(chatMessageMapper::toChatMessageResponseDto)
                .toList();
    }

    // 메시지 ID로 메시지 조회
    public ChatMessageResponseDto getMessageById(Long messageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));
        return chatMessageMapper.toChatMessageResponseDto(chatMessage);
    }

}
