package com.synergy_hub.synergyhub.auth.exception;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class CookieNotFoundException extends CustomException {

    public CookieNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
