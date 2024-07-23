package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.response.MountainResponseDto;

@Data
@Builder
public class MountainSearchRequestDto {

    private Integer pgno;
    private Integer recordSize;
    private String keyword;

}
