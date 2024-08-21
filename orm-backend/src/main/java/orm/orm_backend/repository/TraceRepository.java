package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Trace;

public interface TraceRepository extends JpaRepository<Trace, Integer> {
}
