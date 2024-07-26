package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.TraceCoordinate;

public interface TraceCoordinateRepository extends JpaRepository<TraceCoordinate, Long> {
}
