package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmClubApplicationDto implements FcmAlertData {
    private int applicationId;
    private int clubId;
    private String clubName;
    private int userId;
    private String userName;
    private AlertType alertType;

    @Override
    public String getMessage() {
        return userName + "님이 " + clubName + "모임에 가입 신청 하셨습니다.";
    }
}
