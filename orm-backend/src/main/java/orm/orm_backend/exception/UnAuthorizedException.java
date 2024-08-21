package orm.orm_backend.exception;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super("계정 권한이 유효하지 않습니다.\n다시 로그인을 하세요.");
    }
}
