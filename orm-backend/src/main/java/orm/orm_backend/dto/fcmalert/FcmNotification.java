package orm.orm_backend.dto.fcmalert;

import lombok.Builder;

@Builder
public class FcmNotification {

    private final String title = "오름";
    private final String body;
}
