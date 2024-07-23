package orm.orm_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orm.orm_backend.dto.request.MountainSearchRequestDto;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.service.MountainService;

import java.util.List;

@RestController
@RequestMapping("/mountains")
@Slf4j
@RequiredArgsConstructor
public class MountainController {

    private final MountainService mountainService;

    @GetMapping("/{mountainId}")
    public ResponseEntity<MountainResponseDto> getMountainById(@PathVariable("mountainId") Integer id) {
        log.info("mountainId={}", id);
        MountainResponseDto mountainDto = mountainService.getMountainDtoById(id);
        return ResponseEntity.ok().body(mountainDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MountainResponseDto>> getAllMountains(@ModelAttribute MountainSearchRequestDto mountainSearchRequestDto) {
        List<MountainResponseDto> mountainDtos = mountainService.getAllMountains(mountainSearchRequestDto);
        return ResponseEntity.ok().body(mountainDtos);
    }

}
