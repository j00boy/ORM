package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.TrailDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrailDetailRepository extends JpaRepository<TrailDetail, Integer> {

    /**
     * 고유 trailId를 가진 등산로 세부 좌표들을 반환한다.
     * @param trailId
     * @return 같은 trailId를 갖는 TrailDetail List
     */
    List<TrailDetail> findTrailDetailsByTrailId(Integer trailId);
    List<TrailDetail> findAllByTrailIdIn(List<Integer> trailIds);
}
