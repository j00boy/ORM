package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmClubApplicationDto implements FcmAlertData {
    private String applicationId;
    private String clubId;
    private String clubName;
    private String clubImageSrc;
    private String userId;
    private String userName;
    private final AlertType alertType = AlertType.APPLICATION;

    @Override
    public String getMessage() {
        return userName + "님이 " + clubName + "모임에 가입 신청 하셨습니다.";
    }
}
