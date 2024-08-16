package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmNotification {

    private final String title = "오름";
    private final String body;
}
