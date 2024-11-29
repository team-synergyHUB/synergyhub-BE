package com.synergy_hub.synergyhub.chat.entity;

import com.synergy_hub.synergyhub.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column
    private LocalDateTime deletedAt; // 삭제된 시간

    /**
     * 메시지 삭제 메서드
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now(); // 삭제된 시간 설정
    }

    /**
     * 메시지 삭제 여부 확인 메서드
     *
     * @return 삭제 여부
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public enum MessageType {
        ENTER, QUIT, TALK
    }

    public Long getRoomId() {
        return this.chatRoom.getRoomId();
    }
}
