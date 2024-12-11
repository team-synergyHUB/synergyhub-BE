package com.synergy_hub.synergyhub.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    POST_DELETED(HttpStatus.BAD_REQUEST, "삭제된 게시글입니다."),
    REVIEW_DELETED(HttpStatus.BAD_REQUEST, "삭제된 리뷰입니다."),
    COMMENT_DELETED(HttpStatus.BAD_REQUEST, "삭제된 댓글입니다."),
    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "빈 파일입니다."),
    NO_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "파일 확장자가 없습니다."),  // 스펠링 수정
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 확장자입니다."),  // 스펠링 수정
    AUTH_CODE_EXTENSION(HttpStatus.BAD_REQUEST, "로그인을 실패하였습니다(임시)"),
    INVALID_CHAT_MESSAGE(HttpStatus.BAD_REQUEST, "잘못된 채팅 메시지입니다."),
    EMPTY_CHAT_MESSAGE(HttpStatus.BAD_REQUEST, "채팅 메시지가 비어 있습니다."),
    INVALID_CHAT_ROOM_STATE(HttpStatus.BAD_REQUEST, "유효하지 않은 채팅방 상태입니다."),
    MEMBER_NOT_IN_CHAT_ROOM(HttpStatus.BAD_REQUEST, "사용자가 이 채팅방에 참여하지 않았습니다."),
    EMPTY_CHAT_ROOM(HttpStatus.BAD_REQUEST, "채팅방이 비어 있습니다."),
    UNAUTHORIZED_ACTION(HttpStatus.UNAUTHORIZED, "사용자가 이 작업을 수행할 권한이 없습니다."),


    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 맞지 않습니다."),
    UNAUTHORIZED_CHAT_MESSAGE_UPDATE(HttpStatus.UNAUTHORIZED, "사용자가 메시지를 수정할 권한이 없습니다."),
    UNAUTHORIZED_CHAT_MESSAGE_DELETE(HttpStatus.UNAUTHORIZED, "사용자가 메시지를 삭제할 권한이 없습니다."),

    /* 403 FORBIDDEN : 권한이 없는 사용자 */
    USER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    CHAT_ROOM_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "사용자가 이 채팅방에 접근할 권한이 없습니다."),

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅 메시지를 찾을 수 없습니다."),
//    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅 정보를 찾을 수 없습니다."),
    COOKIE_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠키 정보를 찾을 수 없습니다."),


    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    DUPLICATE_CHAT_ROOM(HttpStatus.CONFLICT, "이미 존재하는 채팅방입니다."),
    DUPLICATE_CHAT_MEMBER(HttpStatus.CONFLICT, "이미 채팅방에 참여 중입니다."),

    /* 410 : GONE : 리소스가 더 이상 유효하지 않음 */
    USER_ALREADY_DELETED(HttpStatus.GONE, "탈퇴된 사용자입니다."),
    PLACE_DELETED(HttpStatus.GONE, "삭제된 장소입니다"),
    JWT_ALREADY_EXPIRED(HttpStatus.GONE, "이미 만료된 토큰입니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 에러 */
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 입출력 오류가 발생했습니다."),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "S3에 객체를 업로드하는 중 예외가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 입출력 오류가 발생했습니다."),
    CHAT_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 서비스에서 오류가 발생했습니다."),
    MESSAGE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "메시지 처리 중 오류가 발생했습니다."),
    DATABASE_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 접근 중 오류가 발생했습니다."),


    /* 팀 관련 에러*/
    LABEL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 라벨입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    MEMBER_NOT_IN_TEAM(HttpStatus.BAD_REQUEST, "팀에 해당 멤버가 존재하지 않습니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    LABEL_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 라벨 ID입니다."),
    MEMBER_TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 멤버가 이 팀에 속해 있지 않습니다."),

    /* 캘린더 관련 에러*/
    CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "캘린더를 찾을 수 없습니다."),
    CALENDAR_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

