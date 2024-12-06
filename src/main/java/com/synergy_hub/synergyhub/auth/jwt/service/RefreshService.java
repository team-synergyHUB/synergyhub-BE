package com.synergy_hub.synergyhub.auth.jwt.service;

import com.synergy_hub.synergyhub.auth.jwt.enitity.RefreshEntity;
import com.synergy_hub.synergyhub.auth.jwt.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshService {

    private final RefreshRepository refreshRepository;

    public Long saveRefresh(String username, String refresh, String expiredMs) {

        RefreshEntity refreshToken = RefreshEntity.createRefresh(username, refresh, expiredMs);
        RefreshEntity savedRefresh = refreshRepository.save(refreshToken);

        return savedRefresh.getId();
    }

    public void deleteRefresh(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }

    @Transactional(readOnly = true)
    public boolean isExistRefresh(String refresh) {
        return refreshRepository.existsByRefresh(refresh);
    }

}
