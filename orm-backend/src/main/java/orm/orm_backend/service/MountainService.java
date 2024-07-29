package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.configuration.Mountain100Config;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.repository.MountainRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MountainService {

    private final MountainRepository mountainRepository;
    private final TrailService trailService;

    // id로 산 조회 -> DTO 반환
    public MountainResponseDto getMountainDtoById(Integer mountainId) {
        Mountain findMountain = mountainRepository.findById(mountainId).orElseThrow();
        List<TrailResponseDto> trailsByMountainId = trailService.getTrailsByMountainId(findMountain.getId());
        MountainResponseDto dto = new MountainResponseDto(findMountain, trailsByMountainId);
        return dto;
    }

    // id로 산 조회 -> Entity 반환
    public Mountain getMountainById(Integer id) {
        Mountain mountainEntity = mountainRepository.findById(id).orElseThrow();
        return mountainEntity;
    }

    // name으로 산 조회
    public List<MountainResponseDto> getAllMountains(String name) {
        List<Mountain> mountains = mountainRepository.findByMountainNameContaining(name);
        List<MountainResponseDto> mountainResponseDtos = new ArrayList<>();
        for(Mountain mountain : mountains) {
            List<TrailResponseDto> trailsByMountainId = trailService.getTrailsByMountainId(mountain.getId());
            MountainResponseDto dto = new MountainResponseDto(mountain, trailsByMountainId);
            mountainResponseDtos.add(dto);
        }
        return mountainResponseDtos;
    }

    // 100대 명산 조회
    public List<MountainResponseDto> get100Mountains() {
        List<String> mountainCodes = Mountain100Config.CODE_100;
        List<Mountain> mountains = mountainRepository.findByMountainCodeIn(mountainCodes);
        List<MountainResponseDto> mountainResponseDtos = new ArrayList<>();
        for(Mountain mountain : mountains) {
            List<TrailResponseDto> trailsByMountainId = trailService.getTrailsByMountainId(mountain.getId());
            MountainResponseDto dto = new MountainResponseDto(mountain, trailsByMountainId);
            mountainResponseDtos.add(dto);
        }
        return mountainResponseDtos;
    }

}
