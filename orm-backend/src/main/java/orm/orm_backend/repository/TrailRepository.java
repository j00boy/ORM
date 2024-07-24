package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.Trail;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Integer> {

    List<Trail> findByMountainId(Integer mountainId);

    Optional<Trail> findById(Integer trailId);

}
