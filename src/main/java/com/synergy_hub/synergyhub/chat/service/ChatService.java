package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.chat.repository.MessageRepository;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    /**
     * 메시지 처리 및 저장
     */
    public void handleMessage(ChatMessage chatMessage, Long memberId) {
        // 채팅방 확인
        chatMessage.setChatRoom(chatRoomRepository.findByRoomId(Long.valueOf(chatMessage.getRoomId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")));

        // Member 설정
        chatMessage.setMember(memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다.")));

        // 메시지 타입에 따른 로직
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getMember().getNickname() + "님이 입장하셨습니다.");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getMember().getNickname() + "님이 퇴장하셨습니다.");
        }

        // 메시지를 데이터베이스에 저장
        messageRepository.save(chatMessage);
    }
}
