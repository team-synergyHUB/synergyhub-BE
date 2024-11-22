package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.dto.MessageRequestDto;
import com.synergy_hub.synergyhub.chat.dto.MessageResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.entity.Message;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public MessageResponseDto sendMessage(Long chatRoomId, MessageRequestDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with id: " + chatRoomId));
        Message message = new Message(
                null,
                chatRoom,
                dto.getMemberId(),
                dto.getType(),
                dto.getDetailMessage(),
                null,
                false
        );
        message = messageRepository.save(message);
        return new MessageResponseDto(
                message.getMessageId(),
                message.getMemberId(),
                "nickname_placeholder", // 실제 닉네임 조회 로직 필요
                message.getType(),
                message.getDetailMessage(),
                message.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }

    public List<MessageResponseDto> getMessages(Long chatRoomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageRepository.findByChatRoom_ChatRoomIdAndIsDeletedFalse(chatRoomId)
                .stream()
                .map(message -> new MessageResponseDto(
                        message.getMessageId(),
                        message.getMemberId(),
                        "nickname_placeholder", // 실제 닉네임 조회 로직 필요
                        message.getType(),
                        message.getDetailMessage(),
                        message.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
                ))
                .collect(Collectors.toList());
    }

    public void deleteMessage(Long chatRoomId, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));
        message.setDeleted(true);
        messageRepository.save(message);
    }
}
