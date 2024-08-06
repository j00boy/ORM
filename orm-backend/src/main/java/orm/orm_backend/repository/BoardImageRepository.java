package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.BoardImage;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Integer> {

    List<BoardImage> findByBoardId(int boardId);
}
