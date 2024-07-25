package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmAcceptanceDto implements FcmAlertData {

    private Integer clubId;
    private String clubName;
    private Boolean isAccepted;

    @Override
    public String getMessage() {
        return clubName + "에 가입" + (isAccepted ? "되었습니다" : "이 거절되었습니다.");
    }
}
