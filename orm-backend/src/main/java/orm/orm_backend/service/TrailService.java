package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.response.TrailDetailResponseDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.TrailDetail;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.repository.TrailDetailRepository;
import orm.orm_backend.repository.TrailRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static orm.orm_backend.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TrailService {

    private final TrailRepository trailRepository;
    private final TrailDetailRepository trailDetailRepository;

    /**
     * mountainId를 가진 Trail Entity들을 List로 조회한 후,
     * TrailResponseDto로 변환하여 List로 반환한다.
     * @param mountainId
     * @return 같은 mountainId를 갖는 Trail을 변환한 TrailResponseDto List
     */
    public List<TrailResponseDto> getTrailsByMountainId(int mountainId) {
        List<Trail> trails = trailRepository.findByMountainId(mountainId);
        return trails.stream()
                .map(trail -> new TrailResponseDto(trail, getAllTrailDetailsByTrailId(trail.getId()))).toList();
    }

    /**
     * trailId로 Trail Entity와 TrailDetail Entity들을 조회한 후,
     * TrailResponseDto로 변환하여 반환한다.
     * @param trailId
     * @return trailId로 조회한 Trail을 변환한 TrailResponseDto
     */
    public TrailResponseDto getTrailById(Integer trailId) {
        Trail trail = trailRepository.findById(trailId).orElseThrow(() -> new CustomException(TRAIL_NOT_FOUND));
        List<TrailDetailResponseDto> trailDetails = trail.getTrailDetails().stream().map(TrailDetailResponseDto::new).toList();
        return new TrailResponseDto(trail, trailDetails);
    }

    /**
     * trailId를 가진 TrailDetail Entity들을 List로 조회한 후,
     * TrailDetailResponseDto로 변환하여 List로 반환한다.
     * @param trailId
     * @return trailId로 조회한 TrailDetail을 변환한 TrailDetailResponseDto List
     */
    public List<TrailDetailResponseDto> getAllTrailDetailsByTrailId(int trailId) {
        List<TrailDetail> findTrailDetails = trailDetailRepository.findTrailDetailsByTrailId(trailId);
        return findTrailDetails.stream().map(TrailDetailResponseDto::new).toList();
    }

    /**
     * trailId를 통해 등산로 Entity를 반환한다.
     * @param trailId
     * @return trailId로 조회한 Trail
     */
    public Trail getTrailEntityById(Integer trailId) {
        return trailRepository.findById(trailId).orElseThrow(() -> new CustomException(TRAIL_NOT_FOUND));
    }

    public List<TrailResponseDto> getAllTrailsByMountainId(Mountain mountain) {
        // trail 조회
        List<Trail> trails = mountain.getTrails();
        // trail Id 추출
        List<Integer> trailIds = trails.stream().map(Trail::getId).toList();

        Map<Integer, Trail> trailsById = new HashMap<>();
        trails.forEach(trail -> trailsById.put(trail.getId(), trail));

        // trailId별 저장할 trailDetails
        Map<Integer, List<TrailDetailResponseDto>> trailDetailsByTrailId = new HashMap<>();
        trailIds.forEach(id -> trailDetailsByTrailId.put(id, new ArrayList<>()));

        // trailId들을 기반으로 전부 조회
        List<TrailDetail> trailDetails = trailDetailRepository.findAllByTrailIdIn(trailIds);

        trailDetails.forEach(trailDetail -> trailDetailsByTrailId.get(trailDetail.getTrail()
                .getId()).add(new TrailDetailResponseDto(trailDetail)));

        return trailIds.stream().map(trailId ->
                new TrailResponseDto(trailsById.get(trailId), trailDetailsByTrailId.get(trailId))).toList();
    }
}
