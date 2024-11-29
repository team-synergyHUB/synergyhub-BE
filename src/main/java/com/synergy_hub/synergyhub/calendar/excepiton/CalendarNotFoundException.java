package com.synergy_hub.synergyhub.calendar.excepiton;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class CalendarNotFoundException extends CustomException {


    public CalendarNotFoundException() {
        super(ErrorCode.CALENDAR_NOT_FOUND);
    }
}
