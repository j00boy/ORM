package orm.orm_backend.dto.fcmalert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FcmAcceptanceDto implements FcmAlertData {

    private String clubId;
    private String clubName;
    private String clubImageSrc;
    @JsonIgnore
    private Boolean isAccepted;
    private AlertType alertType = AlertType.ACCEPTANCE;

    @Override
    public String getMessage() {
        return clubName + "에 가입" + (isAccepted ? "되었습니다" : "이 거절되었습니다.");
    }

    @Builder
    public FcmAcceptanceDto(String clubId, String clubName, String clubImageSrc, Boolean isAccepted) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubImageSrc = clubImageSrc;
        this.isAccepted = isAccepted;
        if (!isAccepted) {
            alertType = AlertType.REJECT;
        }
    }
}
