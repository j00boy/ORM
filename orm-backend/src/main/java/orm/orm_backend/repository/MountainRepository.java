package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.Mountain;

import java.util.Optional;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {

    Optional<Mountain> findById(Integer id);
    Optional<Mountain> findByMountainName(String mountainName);

}
