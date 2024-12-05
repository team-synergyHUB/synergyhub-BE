package com.synergy_hub.synergyhub.auth.exception;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class JwtAlreadyExpiredException extends CustomException {

    public JwtAlreadyExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
