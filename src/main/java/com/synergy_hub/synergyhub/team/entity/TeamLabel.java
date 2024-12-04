package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "team_label")
public class TeamLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    public TeamLabel(Team team, Label label) {
        this.team = team;
        this.label = label;
    }
}
