package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Applicant;

@Data
@Builder
public class ClubJoinDto {
    private Integer clubId;
    private Integer userId;
    private String introduction;
}
