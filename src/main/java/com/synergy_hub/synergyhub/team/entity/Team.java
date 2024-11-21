package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 8)
    private String inviteCode;

    @Column(nullable = false, length = 255)
    private String inviteSecret;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
