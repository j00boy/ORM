package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.repository.MountainRepository;

@Service
@RequiredArgsConstructor
public class MountainService {

    private final MountainRepository mountainRepository;

    // id로 산 조회
    public Mountain getMountainById(Integer id) {
        Mountain findMountain = mountainRepository.findById(id).orElseThrow();
        return findMountain;
    }

    // name으로 산 조회
    public Mountain getMountainByName(String name) {
        Mountain findMountain = mountainRepository.findByMountainName(name).orElseThrow();
        return findMountain;
    }

}
