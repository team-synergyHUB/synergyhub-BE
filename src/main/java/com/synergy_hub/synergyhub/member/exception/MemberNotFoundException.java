package com.synergy_hub.synergyhub.member.exception;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class MemberNotFoundException extends CustomException {

    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
