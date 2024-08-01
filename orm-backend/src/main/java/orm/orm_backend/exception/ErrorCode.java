package orm.orm_backend.exception;

import com.google.api.Http;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다"),
    WITHDRAWN_USER_ID(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "계정 권한이 유효하지 않습니다.\n다시 로그인을 하세요."),

    // Club
    INVALID_CLUB_ID(HttpStatus.BAD_REQUEST, "해당 클럽이 존재하지 않습니다."),
    ALREADY_JOINED(HttpStatus.BAD_REQUEST, "이미 가입한 클럽입니다."),
    ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 가입 신청한 클럽입니다."),
    ;
    private final HttpStatus status;
    private final String message;

}
