package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.Mountain;

import java.util.List;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {

    List<Mountain> findByMountainNameContaining(String name);
    List<Mountain> findByMountainCodeIn(List<String> mountainCodes);

}
