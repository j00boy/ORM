package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Applicant;

@Data
@Builder
public class ApplicantResponseDto {
    private Integer userId;
    private String nickname;
    private String introduction;
    private String imageSrc;


    public static ApplicantResponseDto toDto(Applicant applicant) {
        return ApplicantResponseDto.builder()
                .userId(applicant.getUser().getId())
                .nickname(applicant.getUser().getNickname())
                .introduction(applicant.getIntroduction())
                .imageSrc(applicant.getUser().getImageSrc())
                .build();
    }
}
