package orm.orm_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.Mountain;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {

    Page<Mountain> findByMountainNameContaining(Pageable pageable, String keyword);

}
