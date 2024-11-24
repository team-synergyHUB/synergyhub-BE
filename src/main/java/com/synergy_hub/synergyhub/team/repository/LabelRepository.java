package com.synergy_hub.synergyhub.team.repository;

import com.synergy_hub.synergyhub.team.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {
    // 라벨 이름으로 라벨 조회
    Optional<Label> findByName(String name);

    // 특정 색상의 모든 라벨 조회 (필요할까?)
    List<Label> findAllByColor(String color);
}