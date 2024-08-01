package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.configuration.Mountain100Config;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.dto.response.MountainDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.repository.MountainRepository;

import java.util.List;

import static orm.orm_backend.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MountainService {

    private final MountainRepository mountainRepository;
    private final TrailService trailService;

    /**
     * mountainId로 Mountain Entity와 Trail Entity를 조회한 후,
     * MountainResponseDto로 변환하여 반환한다.
     * @param mountainId
     * @return mountainId로 조회된 Mountain룰 변환한 MountainResponseDto
     */
    public MountainResponseDto getMountainDtoById(Integer mountainId) {
        Mountain mountain = mountainRepository.findById(mountainId).orElseThrow(() -> new CustomException(MOUNTAIN_NOT_FOUND));
        List<TrailResponseDto> trails = trailService.getTrailsByMountainId(mountain.getId());
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
        List<Mountain> mountains = mountainRepository.findByMountainCodeIn(Mountain100Config.CODE_100);
        return mountains.stream().map(MountainDto::new).toList();
    }

}
