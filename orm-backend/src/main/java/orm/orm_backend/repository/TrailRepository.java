package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.Trail;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Integer> {

    /**
     * 같은 mountainId를 가진 등산로 객체들을 반환한다.
     * @param mountainId
     * @return 같은 mountainId를 갖는 Trail List
     */
    List<Trail> findByMountainId(Integer mountainId);
}
