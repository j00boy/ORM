package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.TrailDetail;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrailResponseDto {

    private Integer id;
//    private Float length;
    private String startLatitude;
    private String startLongitude;
    private String peakLatitude;
    private String peakLongitude;
    private List<TrailDetailResponseDto> trailDetails;

    @Builder
    public TrailResponseDto(Trail trail, List<TrailDetailResponseDto> trailDetails) {
        this.id = trail.getId();
//        this.length = trail.getLength();
        this.startLatitude = trail.getStartLatitude();
        this.startLongitude = trail.getStartLongitude();
        this.peakLatitude = trail.getPeakLatitude();
        this.peakLongitude = trail.getPeakLongitude();
        this.trailDetails = (trailDetails != null ? new ArrayList<>() : null);
    }
}
