package orm.orm_backend.exception;

public class UserWithdrawalException extends RuntimeException {
    public UserWithdrawalException() {
        super("이미 탈퇴한 회원입니다.");
    }
}
