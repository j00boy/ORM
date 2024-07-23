package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.TrailDetail;

import java.util.List;

@Data
@Builder
public class TrailResponseDto {

    private Integer id;
    private String startLatitude;
    private String startLongitude;
    private String peakLatitude;
    private String peakLongitude;
    private List<TrailDetail> trailDetails;

}
