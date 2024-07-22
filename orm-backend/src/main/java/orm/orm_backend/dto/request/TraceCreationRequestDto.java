package orm.orm_backend.dto.request;

import lombok.Getter;

import java.sql.Date;

@Getter
public class TraceCreationRequestDto {

    String title;
    Integer mountainId;
    Date hikingDate;
    int trailId;
}
