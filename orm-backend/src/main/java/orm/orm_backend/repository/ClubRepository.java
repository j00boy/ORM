package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Club;

public interface ClubRepository extends JpaRepository<Club, Integer> {

}
