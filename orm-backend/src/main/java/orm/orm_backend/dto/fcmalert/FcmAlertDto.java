package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmAlertDto {
    @Builder.Default
    private Boolean validateOnly = false;
    private FcmMessageDto message;
}
