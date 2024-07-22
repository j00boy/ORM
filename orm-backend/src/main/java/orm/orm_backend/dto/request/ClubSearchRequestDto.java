package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubSearchRequestDto {
    private Integer pgno;
    private Integer recordSize;
    private String keyword;
    private Boolean isMyClub;
}
