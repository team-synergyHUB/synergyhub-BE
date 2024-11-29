package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.dto.ChatResponseDto;
import com.synergy_hub.synergyhub.chat.entity.Chat;
import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.mapper.ChatMapper;
import com.synergy_hub.synergyhub.chat.repository.ChatRepository;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.chat.repository.MessageRepository;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//@RequiredArgsConstructor
//@Service
//public class ChatService {
//
//    private final ChatRoomRepository chatRoomRepository;
//    private final MessageRepository messageRepository;
//    private final MemberRepository memberRepository;
//
//    /**
//     * 메시지 처리 및 저장
//     */
//    public void handleMessage(ChatMessage chatMessage, Long memberId) {
//        // 채팅방 확인
//        chatMessage.setChatRoom(chatRoomRepository.findByRoomId(Long.valueOf(chatMessage.getRoomId()))
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")));
//
//        // Member 설정
//        chatMessage.setMember(memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다.")));
//
//        // 메시지 타입에 따른 로직
//        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
//            chatMessage.setMessage(chatMessage.getMember().getNickname() + "님이 입장하셨습니다.");
//        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
//            chatMessage.setMessage(chatMessage.getMember().getNickname() + "님이 퇴장하셨습니다.");
//        }
//
//        // 메시지를 데이터베이스에 저장
//        messageRepository.save(chatMessage);
//    }
//}


@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMapper chatMapper;

    // 채팅방 입장
    public ChatResponseDto enterChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 중복 여부 확인
        chatRepository.findByChatRoomAndMember(chatRoom, member)
                .ifPresent(chat -> {
                    throw new CustomException(ErrorCode.DUPLICATE_CHAT_MEMBER);
                });

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();

        Chat savedChat = chatRepository.save(chat);
        return chatMapper.toChatResponseDto(savedChat);
    }

    // 참여한 채팅 목록 조회
    public List<ChatResponseDto> getChats(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<Chat> chats = chatRepository.findByMember(member);
        return chats.stream()
                .map(chatMapper::toChatResponseDto)
                .toList();
    }

    // 채팅방 퇴장
    public Long exitChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Chat chat = chatRepository.findByChatRoomAndMember(chatRoom, member)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));

        chatRepository.delete(chat);
        return chatRoomId;
    }
}

