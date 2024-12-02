package com.synergy_hub.synergyhub.auth.oauth.service;

import com.synergy_hub.synergyhub.auth.oauth.dto.CustomOauth2User;
import com.synergy_hub.synergyhub.auth.oauth.dto.GoogleResponse;
import com.synergy_hub.synergyhub.auth.oauth.dto.OAuth2Response;
import com.synergy_hub.synergyhub.auth.oauth.dto.UserDto;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberRole;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);
        log.info("Oauth2User={}", user);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(user.getAttributes());
        } else {
            return null;
        }

        //google (providerId) - 고유 아이디
        String uid = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String nickname = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();

        UserDto userDto = new UserDto(uid, email, nickname, MemberRole.USER);

        memberRepository.findByNicknameAndEmailDeletedAtIsNull(nickname, email)
            .ifPresentOrElse(
                m -> m.updateMyInfo(nickname),

                () -> {
                    Member member = Member.createMember(nickname, email, "null");
                    member.changeRole(MemberRole.USER);
                    memberRepository.save(member);
                });

        return new CustomOauth2User(userDto);

    }


}
