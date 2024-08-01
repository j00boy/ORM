package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmMessageDto {
    private String token;
    private FcmAlertData data;
    private FcmNotification notification;
}
