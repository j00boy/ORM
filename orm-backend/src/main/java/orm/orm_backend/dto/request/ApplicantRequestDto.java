package orm.orm_backend.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orm.orm_backend.entity.Applicant;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.User;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicantRequestDto {
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

    @Builder
    public ApplicantRequestDto(Applicant applicant) {
        this.clubId = applicant.getClub().getId();
        this.userId = applicant.getUser().getId();
        this.introduction = applicant.getIntroduction();
    }
}
