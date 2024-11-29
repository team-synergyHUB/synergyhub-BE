package com.synergy_hub.synergyhub.member.exception;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class MemberNotAuthenticatedException extends CustomException {

    public MemberNotAuthenticatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
