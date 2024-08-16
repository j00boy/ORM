package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmBoardDto implements FcmAlertData {

    private String clubId;
    private String boardId;
    private String clubName;
    private String userName;
    private String userId;
    private String title;
    private final AlertType alertType = AlertType.NEW_BOARD;

    @Override
    public String getMessage() {
        return userName + "님이 " + clubName + " 게시판에 새 게시글을 작성하였습니다.";
    }
}
