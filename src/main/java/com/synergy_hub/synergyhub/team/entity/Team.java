package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @ManyToOne(optional = true) // 연관 관계에서 null 허용
    @JoinColumn(name = "label_id", nullable = true) // DB에서 nullable 허용
    private Label label;

    public void markAsDeleted() {
        this.isDeleted = false;
    }

    public void updateTeam(String name, String inviteCode, String inviteSecret, Label label) {
        this.name = name;
        this.inviteCode = inviteCode;
        this.inviteSecret = inviteSecret;
        this.label = label;
        this.isDeleted = false; // 명시적으로 기본값 설정
    }
}