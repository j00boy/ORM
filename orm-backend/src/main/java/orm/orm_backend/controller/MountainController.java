package orm.orm_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.dto.response.MountainDto;
import orm.orm_backend.dto.response.MountainSimpleResponseDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.service.MountainService;
import orm.orm_backend.service.RedisService;
import orm.orm_backend.service.TrailService;

import java.util.List;

@RestController
@RequestMapping("/mountains")
@Slf4j
@RequiredArgsConstructor
public class MountainController {

    private final String REDIS_PREFIX = "mountains::";

    private final MountainService mountainService;
    private final TrailService trailService;
    private final RedisService redisService;

    @GetMapping("/all")
    public ResponseEntity<List<MountainSimpleResponseDto>> getAllMountains() {
        String cacheKey = REDIS_PREFIX + "all";

        List<MountainSimpleResponseDto> mountainDtos = redisService.getList(cacheKey);
        if (mountainDtos == null) {
            mountainDtos = mountainService.getAllMountains();

            redisService.addAllList(cacheKey, mountainDtos);
        }
        return ResponseEntity.ok().body(mountainDtos);

    }

    @GetMapping("/top")
    public ResponseEntity<List<MountainDto>> get100Mountains() {
        String cacheKey = REDIS_PREFIX + "top100";

        List<MountainDto> mountainDtos = redisService.getList(cacheKey);
        if (mountainDtos == null) {
            mountainDtos = mountainService.get100Mountains();

            redisService.addAllList(cacheKey, mountainDtos);
        }
        return ResponseEntity.ok().body(mountainDtos);
    }

    @GetMapping("/{mountainId}")
    public ResponseEntity<MountainResponseDto> getMountainById(@PathVariable("mountainId") Integer id, boolean trailContaining) {
        String cacheKey = REDIS_PREFIX + id;
        if (!trailContaining) {
            cacheKey = REDIS_PREFIX + "::trailX";
        }

        MountainResponseDto mountainDto = redisService.getObject(cacheKey, MountainResponseDto.class);
        if (mountainDto == null) {
            mountainDto = mountainService.getMountainDtoById(id, trailContaining);

            redisService.saveObject(cacheKey, mountainDto);
        }
        return ResponseEntity.ok().body(mountainDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MountainDto>> getMountainsBySearch(String name) {
        String cacheKey = REDIS_PREFIX + name;

        List<MountainDto> mountains = redisService.getList(cacheKey);
        if (mountains == null) {
            mountains = mountainService.getMountainsBySearch(name);

            redisService.addAllList(cacheKey, mountains);
        }

        return ResponseEntity.ok().body(mountains);
    }

    @GetMapping("/trail/{trailId}")
    public ResponseEntity<TrailResponseDto> getTrailDetails(@PathVariable("trailId") Integer trailId) {
        TrailResponseDto trailResponseDto = trailService.getTrailById(trailId);
        return ResponseEntity.ok().body(trailResponseDto);
    }
}
