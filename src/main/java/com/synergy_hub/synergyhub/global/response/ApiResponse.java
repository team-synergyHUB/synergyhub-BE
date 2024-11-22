package com.synergy_hub.synergyhub.global.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private String message;  //응답 메세지
    private T payload;  //데이터 페이로드
    private String error; //에러 (실패 시)

}
