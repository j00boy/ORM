package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
