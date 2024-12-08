package com.synergy_hub.synergyhub.auth.oauth.dto;

public interface OAuth2Response {

    //google, kakao ...
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    //프로필 사진
    String getPicture();

}
