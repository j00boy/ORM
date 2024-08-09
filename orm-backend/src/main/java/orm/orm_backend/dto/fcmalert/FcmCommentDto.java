package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmCommentDto implements FcmAlertData {

    String commentId;
    String boardId;
    String title;
    String userId;
    String userName;
    private final AlertType alertType = AlertType.NEW_COMMENT;

    @Override
    public String getMessage() {
        return userName + "님이 새 댓글을 작성하였습니다.";
    }
}
