package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Applicant;

@Data
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
