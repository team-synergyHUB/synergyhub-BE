package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA에서만 사용
@AllArgsConstructor
@Entity
@Table(name = "label")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name; // 라벨 이름

    @Column(nullable = false, length = 7)
    private String color; // 라벨 색상

    public Label(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // 엔티티 필드 값을 변경하는 메서드
    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // 팀 추가 메서드
    public void addTeam(Team team) {
        if (!this.teams.contains(team)) {
            this.teams.add(team);
        }
    }

    @ManyToMany(mappedBy = "labels") // Team 엔티티와 매핑
    private List<Team> teams = new ArrayList<>();
}
