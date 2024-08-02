package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmExpelDto implements FcmAlertData {
    private String clubId;
    private String clubName;
    private String clubImageSrc;
    private final AlertType alertType;

    @Override
    public String getMessage() {
        return clubName + "에서 추방되었습니다.";
    }
}
