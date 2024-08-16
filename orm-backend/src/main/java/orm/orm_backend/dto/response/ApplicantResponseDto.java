package orm.orm_backend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orm.orm_backend.entity.Applicant;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicantResponseDto {
    private Integer userId;
    private String nickname;
    private String introduction;
    private String imgSrc;

    @Builder
    public ApplicantResponseDto(Applicant applicant) {
        this.userId = applicant.getUser().getId();
        this.nickname = applicant.getUser().getNickname();
        this.introduction = applicant.getIntroduction();
        this.imgSrc = applicant.getUser().getImageSrc();
    }
}
