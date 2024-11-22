package com.synergy_hub.synergyhub.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String type; // ENUM TEXT or IMAGE

    @Column(nullable = true)
    private String detailMessage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

}
