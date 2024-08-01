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
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.service.MountainService;
import orm.orm_backend.service.TrailService;

import java.util.List;

@RestController
@RequestMapping("/mountains")
@Slf4j
@RequiredArgsConstructor
public class MountainController {

    private final MountainService mountainService;
    private final TrailService trailService;

    @GetMapping("/top")
    public ResponseEntity<List<MountainDto>> get100Mountains() {
        List<MountainDto> mountainDtos = mountainService.get100Mountains();
        return ResponseEntity.ok().body(mountainDtos);
    }

    @GetMapping("/{mountainId}")
    public ResponseEntity<MountainResponseDto> getMountainById(@PathVariable("mountainId") Integer id) {
        MountainResponseDto mountainDto = mountainService.getMountainDtoById(id);
        return ResponseEntity.ok().body(mountainDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MountainDto>> getMountainsBySearch(String name) {
        List<MountainDto> mountains = mountainService.getMountainsBySearch(name);
        return ResponseEntity.ok().body(mountains);
    }

    @GetMapping("/trail/{trailId}")
    public ResponseEntity<TrailResponseDto> getTrailDetails(@PathVariable("trailId") Integer trailId) {
        TrailResponseDto trailResponseDto = trailService.getTrailById(trailId);
        return ResponseEntity.ok().body(trailResponseDto);
    }
}
