package com.synergy_hub.synergyhub.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@Getter
//@Setter
//public class ChatMessage {
//    // 메시지 타입 : 입장, 채팅
//    public enum MessageType {
//        ENTER, JOIN, QUIT, TALK
//    }
//    private MessageType type; // 메시지 타입
//    private String roomId; // 방번호
//    private String sender; // 메시지 보낸사람
//    private String message; // 메시지
//}


import com.synergy_hub.synergyhub.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId; // 메시지 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom; // 채팅방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 메시지를 보낸 회원

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type; // 메시지 타입 (ENTER, QUIT, TALK 등)

    @Column(nullable = true)
    private String message; // 메시지 내용

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시간

    @Column(nullable = false)
    private boolean isDeleted = false; // 삭제 여부

    public void delete() {
        this.isDeleted = true;
    }

    public enum MessageType {
        ENTER, QUIT, TALK
    }

    public Long getRoomId() {
        return this.chatRoom.getRoomId();
    }

}
