package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.Mountain;

import java.util.List;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {

    /**
     * name이 포함된 산 객체들을 반환한다.
     * @param name
     * @return
     */
    List<Mountain> findByMountainNameContaining(String name);

    /**
     * mountainCodes 리스트 안에 포함된 산 코드를 가진 산 객체들을 반환한다.
     * @param mountainCodes
     * @return
     */
    List<Mountain> findByMountainCodeIn(List<String> mountainCodes);

}
