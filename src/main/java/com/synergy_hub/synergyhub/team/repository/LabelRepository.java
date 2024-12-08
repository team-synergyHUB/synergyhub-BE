package com.synergy_hub.synergyhub.team.repository;

import com.synergy_hub.synergyhub.team.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    // 라벨 이름으로 라벨 조회
    Optional<Label> findByName(String name);

    // 특정 색상의 모든 라벨 조회 (필요할까?)
    List<Label> findAllByColor(String color);

    @Query("SELECT l FROM Label l JOIN l.teams t WHERE t.id = :teamId")
    List<Label> findLabelsByTeamId(@Param("teamId") Long teamId);

//    @Modifying
//    @Query("DELETE FROM Team t JOIN t.labels l WHERE l.id = :labelId")
//    void deleteByLabelId(@Param("labelId") Long labelId);

//    Long deleteByLabelId(Long labelId);


//    void deleteByLabelId(Long labelId);
}
