package orm.orm_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Club;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    List<Club> findAllByClubNameContaining(String keyword);
    List<Club> findAllByMountainId(Integer mountainId);
    Boolean existsByClubName(String clubName);
    List<Club> findAllByIdIn(List<Integer> ids);
}
