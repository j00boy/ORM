package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.TraceImage;

public interface TraceImageRepository extends JpaRepository<TraceImage, Integer> {
}
