package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.TrailDetail;

@Data
@Builder
public class TrailDetailResponseDto {

    private String latitude;
    private String longitude;
    private Integer difficulty;

    public static TrailDetailResponseDto toTrailDetailResponseDto(TrailDetail trailDetail) {
        return TrailDetailResponseDto.builder()
                .latitude(trailDetail.getLatitude())
                .longitude(trailDetail.getLongitude())
                .difficulty(trailDetail.getDifficulty())
                .build();
    }

}
