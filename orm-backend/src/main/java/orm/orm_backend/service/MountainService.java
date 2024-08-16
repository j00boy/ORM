package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import orm.orm_backend.configuration.Mountain100Config;
import orm.orm_backend.dto.response.MountainDto;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.dto.response.MountainSimpleResponseDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.TrailDetail;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.repository.MountainRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static orm.orm_backend.exception.ErrorCode.MOUNTAIN_NOT_FOUND;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "mountains")
public class MountainService {

    private final String elevationApiUrl = "https://api.open-elevation.com/api/v1/lookup?locations=";

    private final MountainRepository mountainRepository;
    private final TrailService trailService;

    /**
     * mountainId로 Mountain Entity와 Trail Entity를 조회한 후,
     * MountainResponseDto로 변환하여 반환한다.
     * @param mountainId
     * @return mountainId로 조회된 Mountain룰 변환한 MountainResponseDto
     */
    public MountainResponseDto getMountainDtoById(Integer mountainId, boolean trailContaining) {
        Mountain mountain = mountainRepository.findById(mountainId).orElseThrow(() -> new CustomException(MOUNTAIN_NOT_FOUND));
        List<TrailResponseDto> trails = null;
        if (trailContaining) {
            trails = trailService.getAllTrailsByMountainId(mountain);
        }
        return new MountainResponseDto(mountain, trails);
    }


    /**
     * id로 Mountain Entity를 조회한다.
     * @param id
     * @return id로 조회된 Mountain
     */
    public Mountain getMountainById(Integer id) {
        return mountainRepository.findById(id).orElseThrow(() -> new CustomException(MOUNTAIN_NOT_FOUND));
    }

    /**
     * mountainName에 (name) 검색어로 포함된 Mountain Entity들을 List로 조회한 후,
     * MountainDto로 변환하여 List로 반환한다.
     * @param name
     * @return 'name'으로 조회된 Mountain List를 변환한 MountainDto List
     */
    public List<MountainDto> getMountainsBySearch(String name) {
        List<Mountain> mountains = mountainRepository.findByMountainNameContaining(name);
        return mountains.stream().map(MountainDto::new).toList();
    }

    /**
     * 100대 명산 중 상위 20개에 속하는 Mountain Entity들을 조회한 후,
     * MountainDto로 변환하여 List로 반환한다.
     * @return '100대 명산'에 속하는 Mountain을 변환한 MountainDto List
     */
    public List<MountainDto> get100Mountains() {
        // 불변 리스트를 Copy
        List<Mountain> mountains = new ArrayList<>(mountainRepository.findByMountainCodeIn(Mountain100Config.CODE_100));

        // 무작위로 요소들의 순서를 섞음
        Collections.shuffle(mountains);

        // 20개까지만 이름 순서대로 List를 자름
        return mountains.stream()
                .limit(20)
                .sorted(Comparator.comparing(Mountain::getMountainName))
                .map(MountainDto::new).toList();
    }

    public List<MountainSimpleResponseDto> getAllMountains() {
        List<Mountain> all = mountainRepository.findAll();
        return all.stream().map(MountainSimpleResponseDto::new).toList();
    }

    @Transactional
    public void updateExcludedData() throws JsonProcessingException {
        List<Mountain> mountains = mountainRepository.findAll();

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Mountain mountain : mountains) {
            List<Trail> trails = mountain.getTrails();
            if (trails.isEmpty()) {
                continue;
            }
            Trail trail = trails.get(0);
            List<TrailDetail> trailDetails = trail.getTrailDetails();
            TrailDetail trailDetail = trailDetails.get(0);
            String peakLatitude = trailDetail.getLatitude();
            String peakLongitude = trailDetail.getLongitude();
            mountain.updateAddressCoordinates(peakLatitude, peakLongitude);

            String elevationResponse = restTemplate.getForObject(elevationApiUrl + peakLatitude + "," + peakLongitude, String.class);
            JsonNode rootNode = objectMapper.readTree(elevationResponse);
            JsonNode results = rootNode.path("results");
            results.get(0);
            String elevation = results.get(0).path("elevation").asText();
            mountain.updateAltitude(Float.valueOf(elevation));
        }
    }

    public Integer getRecommendMountainIdOfToday() {
        // 불변 리스트를 Copy
        List<Mountain> mountains = new ArrayList<>(mountainRepository.findByMountainCodeIn(Mountain100Config.CODE_100));

        // 무작위로 요소들의 순서를 섞음
        Collections.shuffle(mountains);

        // 1개의 id만 return
        return mountains.get(0).getId();
    }
}
