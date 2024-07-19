package orm.orm_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.repository.ClubRepository;
import orm.orm_backend.repository.MountainRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubServiceImpl implements ClubService{
    private final ClubRepository clubRepository;
    private final MountainRepository mountainRepository;

    @Override
    public Integer createClub(ClubRequestDto clubRequestDTO) {
        return 0;
    }
}
