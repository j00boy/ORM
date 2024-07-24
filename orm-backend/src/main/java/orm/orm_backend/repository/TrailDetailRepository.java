package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.TrailDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrailDetailRepository extends JpaRepository<TrailDetail, Integer> {

    List<TrailDetail> findTrailDetailsByTrailId(Integer trailId);

}
