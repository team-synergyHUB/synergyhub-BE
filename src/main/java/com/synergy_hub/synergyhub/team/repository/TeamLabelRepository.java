package com.synergy_hub.synergyhub.team.repository;

import com.synergy_hub.synergyhub.team.entity.TeamLabel;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TeamLabelRepository extends JpaRepository<TeamLabel, Long> {

    void deleteByLabelId(@Param("labelId") Long labelId);

    void deleteByTeamId(@Param("teamId") Long teamId);
}
