package com.synergy_hub.synergyhub.calendar.excepiton;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;

public class CalendarEventNotFoundException extends CustomException {

    public CalendarEventNotFoundException() {
        super(ErrorCode.CALENDAR_EVENT_NOT_FOUND);
    }
}
