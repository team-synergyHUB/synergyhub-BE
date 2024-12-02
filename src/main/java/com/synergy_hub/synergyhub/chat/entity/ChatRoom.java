package com.synergy_hub.synergyhub.chat.entity;

import com.synergy_hub.synergyhub.team.entity.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, unique = true)
    private Team team;

//    @Column(nullable = false)
//    private String roomName;

//    @Column(nullable = false)
//    private String roomState;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt; // 삭제된 시간

    /**
     * 채팅방 삭제 메서드
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 채팅방 삭제 여부 확인 메서드
     * @return 삭제 여부
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }
}
