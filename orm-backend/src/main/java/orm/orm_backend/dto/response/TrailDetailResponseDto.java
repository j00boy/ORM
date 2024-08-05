package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.TrailDetail;

@Data
public class TrailDetailResponseDto {

    private String x; // latitude
    private String y; // longitude
    private Integer d; // difficulty

    @Builder
    public TrailDetailResponseDto(TrailDetail trailDetail) {
        this.x = trailDetail.getLatitude();
        this.y = trailDetail.getLongitude();
        this.d = trailDetail.getDifficulty();
    }

}
