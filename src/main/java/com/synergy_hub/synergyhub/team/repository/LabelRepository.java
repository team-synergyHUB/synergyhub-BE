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

    @Query("SELECT l FROM Label l JOIN l.teams t WHERE t.id = :teamId")
    List<Label> findLabelsByTeamId(@Param("teamId") Long teamId);
}
