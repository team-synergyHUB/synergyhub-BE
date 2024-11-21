package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name; // 팀 이름

    @Column(nullable = false, length = 8)
    private String inviteCode; // 초대 코드

    @Column(nullable = false, length = 30)
    private String inviteSecret; // 초대 비밀번호


    @Column(nullable = false)
    private Boolean isDeleted = false; // 삭제 여부

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = true)
    private Label label; // 팀과 연결된 라벨
}

