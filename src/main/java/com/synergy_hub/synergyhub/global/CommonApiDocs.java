package com.synergy_hub.synergyhub.global;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 공통 Swagger 문서화를 위한 어노테이션.
 * 각 컨트롤러 메서드에서 사용 가능.
 */
@Target({ElementType.METHOD}) // 메서드에만 적용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
@Operation // Swagger 문서화의 기본 Operation
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
})
public @interface CommonApiDocs {
    String summary();      // API 요약
    String description();  // API 상세 설명
}
