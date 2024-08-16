package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orm.orm_backend.entity.TrailDetail;

@Data
@NoArgsConstructor
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
