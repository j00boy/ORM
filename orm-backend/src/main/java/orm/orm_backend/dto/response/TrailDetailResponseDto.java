package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.TrailDetail;

@Data
public class TrailDetailResponseDto {

    private String latitude;
    private String longitude;
    private Integer difficulty;

    @Builder
    public TrailDetailResponseDto(TrailDetail trailDetail) {
        this.latitude = trailDetail.getLatitude();
        this.longitude = trailDetail.getLongitude();
        this.difficulty = trailDetail.getDifficulty();
    }

}
