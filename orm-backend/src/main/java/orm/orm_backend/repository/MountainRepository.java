package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.Mountain;

import java.util.List;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, Integer> {

    /**
     * name이 포함된 산 객체들을 반환한다.
     * @param name
     * @return 'name'으로 조회된 Mountain List
     */
    List<Mountain> findByMountainNameContaining(String name);

    /**
     * mountainCodes 리스트 안에 포함된 산 코드를 가진 산 객체들을 반환한다.
     * @param mountainCodes
     * @return '100대 명산'에 속하는 Mountain List
     */
    @Query("select m from Mountain m where m.mountainCode in :mountainCodes and m.id in (select distinct t.mountain.id from Trail t)")
    List<Mountain> findByMountainCodeIn(List<String> mountainCodes);

}
