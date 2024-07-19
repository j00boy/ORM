package orm.orm_backend.service;

import orm.orm_backend.dto.request.ClubRequestDto;

public interface ClubService {
    Integer createClub(ClubRequestDto clubRequestDTO);
}
