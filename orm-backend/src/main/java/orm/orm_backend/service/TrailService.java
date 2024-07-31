package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.response.TrailDetailResponseDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.TrailDetail;
import orm.orm_backend.repository.TrailDetailRepository;
import orm.orm_backend.repository.TrailRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrailService {

    private final TrailRepository trailRepository;
    private final TrailDetailRepository trailDetailRepository;

    // 산ID로 모든 등산로 조회
    public List<TrailResponseDto> getTrailsByMountainId(int mountainId) {
        List<Trail> findTrails = trailRepository.findByMountainId(mountainId);
        List<TrailResponseDto> trailResponseDtos = new ArrayList<>();
        findTrails.forEach(trail -> trailResponseDtos.add(new TrailResponseDto(trail, getAllTrailDetailsByTrailId(trail.getId()))));
        return trailResponseDtos;
    }

    public TrailResponseDto getTrailById(Integer trailId) {
        Trail trail = trailRepository.findById(trailId).orElseThrow();
        List<TrailDetail> findTrailDetails = trailDetailRepository.findTrailDetailsByTrailId(trailId);
        List<TrailDetailResponseDto> trailDetailResponseDtos = new ArrayList<>();
        findTrailDetails.forEach(trailDetail -> trailDetailResponseDtos.add(new TrailDetailResponseDto(trailDetail)));
        return new TrailResponseDto(trail, trailDetailResponseDtos);
    }

    // 등산로 ID로 등산로 상세 조회
    public List<TrailDetailResponseDto> getAllTrailDetailsByTrailId(int trailId) {
        List<TrailDetail> findTrailDetails = trailDetailRepository.findTrailDetailsByTrailId(trailId);
        List<TrailDetailResponseDto> trailDetailResponseDtos = new ArrayList<>();
        findTrailDetails.forEach(trailDetail -> trailDetailResponseDtos.add(new TrailDetailResponseDto(trailDetail)));
        return trailDetailResponseDtos;
    }
    
    // Entity만 반환
    public Trail getTrailEntityById(Integer trailId) {
        Trail trail = trailRepository.findById(trailId).orElseThrow();
        return trail;
    }

}
