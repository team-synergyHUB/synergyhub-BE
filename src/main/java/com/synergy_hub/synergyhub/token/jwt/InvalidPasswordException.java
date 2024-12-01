package com.synergy_hub.synergyhub.token.jwt;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class InvalidPasswordException extends CustomException {

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
