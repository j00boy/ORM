package orm.orm_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.service.MountainService;

@RestController
@RequestMapping("/mountains")
public class MountainController {

    private MountainService mountainService;

    @GetMapping
    public ResponseEntity<MountainResponseDto> getMountainById(@PathVariable("mountainId") Integer id) {
        Mountain mountain = mountainService.getMountainById(id);

    }

}
