package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Getter
@Builder
public class TraceRequestDto {

    private Integer id; // 생성 시에는 null, 수정시에는 id를 담아 준다.
    private String title;
    private Integer mountainId;
    private String hikingDate;
    private Integer trailId;
}
