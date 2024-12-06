package com.synergy_hub.synergyhub.auth.jwt.repository;

import com.synergy_hub.synergyhub.auth.jwt.enitity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);
}
