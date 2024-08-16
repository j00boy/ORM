package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubSearchRequestDto {
    private String keyword;
    private Boolean isMyClub;
}
