package com.synergy_hub.synergyhub.auth.oauth.service;

import com.synergy_hub.synergyhub.auth.oauth.dto.CustomOauth2User;
import com.synergy_hub.synergyhub.auth.oauth.dto.GoogleResponse;
import com.synergy_hub.synergyhub.auth.oauth.dto.OAuth2Response;
import com.synergy_hub.synergyhub.auth.oauth.dto.UserDto;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.entity.MemberRole;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import java.util.Map;
import java.util.Optional;
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
        String nickname = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();
        String profileImage = oAuth2Response.getPicture();

        // 사용자 조회
        Optional<Member> memberOpt = memberRepository.
            findByEmailAndDeletedAtIsNull(email);
//            findByNicknameAndEmailDeletedAtIsNull(nickname, email);

        Member member = null;

        if (memberOpt.isPresent()) {
            member = memberOpt.get();
            member.updateMyInfo(nickname);
//            userDto = new UserDto(member.getId(), email, nickname, MemberRole.USER);
        } else {
            // 새 사용자 생성
            member = Member.createMember(nickname, email, "null");
            member.changeRole(MemberRole.USER);
            member.updateProfileImage(profileImage);
            memberRepository.save(member);
//            userDto = new UserDto( member.getId(), email, nickname, MemberRole.USER);
        }

        return new MemberDetails(member);
    }


}
