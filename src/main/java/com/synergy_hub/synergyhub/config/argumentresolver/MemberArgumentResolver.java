package com.synergy_hub.synergyhub.config.argumentresolver;

import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.exception.MemberNotFoundException;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(AuthenticatedMember.class);

        // 파라미터 타입이 Member 또는 MemberDetails인지 확인
        boolean isSupportedType = Member.class.isAssignableFrom(parameter.getParameterType()) ||
            MemberDetails.class.isAssignableFrom(parameter.getParameterType());

        return hasParameterAnnotation && isSupportedType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        MemberDetails principal = getMemberDetails();

        if (Member.class.isAssignableFrom(parameter.getParameterType())) {
            return memberRepository.findByIdAndDeletedAtIsNull(principal.getUserId())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.USER_NOT_FOUND));
        } else if (MemberDetails.class.isAssignableFrom(parameter.getParameterType())) {
            return principal;
        }

        throw new IllegalArgumentException("not supported parameter");
    }

    private static MemberDetails getMemberDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MemberNotFoundException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        if (!(authentication.getPrincipal() instanceof MemberDetails)) {
            throw new MemberNotFoundException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return principal;
    }
}
