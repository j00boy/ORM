package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Trail;

import java.util.List;

@Data
@Builder
public class TrailResponseDto {

    private Integer id;
    private Float distance;
    private Float heuristic;
    private String startLatitude;
    private String startLongitude;
    private String peakLatitude;
    private String peakLongitude;
    private List<TrailDetailResponseDto> trailDetails;

    public TrailResponseDto toTrailResponseDto(Trail trail, List<TrailDetailResponseDto> trailDetails) {
        return TrailResponseDto.builder()
                .id(trail.getId())
                .distance(trail.getDistance())
                .heuristic(trail.getHeuristic())
                .startLatitude(trail.getStartLatitude())
                .startLongitude(trail.getStartLongitude())
                .peakLatitude(trail.getPeakLatitude())
                .peakLongitude(trail.getPeakLongitude())
                .build();
    }
}
