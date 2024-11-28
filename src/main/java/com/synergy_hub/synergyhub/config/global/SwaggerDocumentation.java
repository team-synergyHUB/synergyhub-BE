package com.synergy_hub.synergyhub.config.global;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class SwaggerDocumentation {

    // 공통 응답 정의
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public @interface CommonResponses {}

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "팀 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public @interface CreateTeamResponses {}

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 수정 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public @interface UpdateTeamResponses {}

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "팀 나가기 성공"),
            @ApiResponse(responseCode = "404", description = "팀 또는 멤버를 찾을 수 없음")
    })
    public @interface LeaveTeamResponses {}
}
