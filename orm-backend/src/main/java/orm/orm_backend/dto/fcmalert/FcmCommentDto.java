package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmCommentDto implements FcmAlertData {

    private String clubId;
    private String commentId;
    private String boardId;
    private String title;
    private String userId;
    private String userName;
    private final AlertType alertType = AlertType.NEW_COMMENT;

    @Override
    public String getMessage() {
        return userName + "님이 새 댓글을 작성하였습니다.";
    }
}
