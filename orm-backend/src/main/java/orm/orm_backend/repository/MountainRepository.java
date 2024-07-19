package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Mountain;

public interface MountainRepository extends JpaRepository<Mountain, Integer> {
}
