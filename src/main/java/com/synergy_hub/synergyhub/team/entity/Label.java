package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
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
        this.name = name; // 라벨 이름 변경
        this.color = color; // 라벨 색상 변경
    }

    @OneToMany(mappedBy = "label")
    private List<Team> teams = new ArrayList<>(); // 라벨과 연결된 팀 목록
}
