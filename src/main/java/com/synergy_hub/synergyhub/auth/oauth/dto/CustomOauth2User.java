package com.synergy_hub.synergyhub.auth.oauth.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOauth2User implements OAuth2User {

    private final UserDto userDto;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();
        collections.add(() -> {
            return userDto.getRole().getValue();
        });

        return collections;
    }

    @Override
    public String getName() {
        return userDto.getName();  //nickname
    }

    public String getEmail() {
        return userDto.getEmail();
    }

    public Long getUserId() {
        return userDto.getUserId();
    }
}
