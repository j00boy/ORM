package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.TraceImage;

import java.util.List;

public interface TraceImageRepository extends JpaRepository<TraceImage, Integer> {
    List<TraceImage> findByTraceId(Integer traceId);
}