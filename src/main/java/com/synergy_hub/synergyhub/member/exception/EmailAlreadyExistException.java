package com.synergy_hub.synergyhub.member.exception;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class EmailAlreadyExistException extends CustomException {

    public EmailAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
