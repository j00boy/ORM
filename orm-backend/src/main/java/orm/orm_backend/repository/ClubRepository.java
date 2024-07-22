package orm.orm_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Club;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    Page<Club> findAllByClubNameContaining(Pageable pageable, String keyword);
}
