package orm.orm_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import orm.orm_backend.dto.response.MountainResponseDto;
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

    @GetMapping("/{mountainId}")
    public ResponseEntity<MountainResponseDto> getMountainById(@PathVariable("mountainId") Integer id) {
        MountainResponseDto mountainDto = mountainService.getMountainDtoById(id);
        return ResponseEntity.ok().body(mountainDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MountainResponseDto>> getAllMountains(String name) {
        List<MountainResponseDto> mountainDtos = mountainService.getAllMountains(name);
        return ResponseEntity.ok().body(mountainDtos);
    }

    @GetMapping("/trail/{trailId}")
    public ResponseEntity<TrailResponseDto> getTrailDetails(@PathVariable("trailId") Integer trailId) {
        TrailResponseDto trailResponseDto = trailService.getTrailById(trailId);
        return ResponseEntity.ok().body(trailResponseDto);
    }
}
