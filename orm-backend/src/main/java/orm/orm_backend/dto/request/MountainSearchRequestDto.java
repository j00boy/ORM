package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MountainSearchRequestDto {

    private Integer pgno;
    private Integer recordSize;
    private String keyword;

}
