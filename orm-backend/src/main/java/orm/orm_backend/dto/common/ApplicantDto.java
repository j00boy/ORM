package orm.orm_backend.dto.common;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Applicant;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.User;

@Data
@Builder
public class ApplicantDto {
    private Integer clubId;
    private Integer userId;
    private String introduction;

    public Applicant toEntity(User user, Club club) {
        return Applicant.builder()
                .user(user)
                .club(club)
                .introduction(introduction)
                .build();
    }

    public static ApplicantDto toDto(Applicant applicant) {
        return ApplicantDto.builder()
                .clubId(applicant.getClub().getId())
                .userId(applicant.getUser().getId())
                .introduction(applicant.getIntroduction())
                .build();
    }
}
