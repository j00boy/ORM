package orm.orm_backend.dto.request;

import lombok.Getter;

import java.sql.Date;

@Getter
public class TraceRequestDto {

    Integer id; // 생성 시에는 null, 수정시에는 id를 담아 준다.
    String title;
    Integer mountainId;
    Date hikingDate;
    int trailId;
}
