package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    /**
     * 특정 clubId에 속한 게시글들을 모두 조회합니다.
     * @param clubId
     * @return
     */
    List<Board> findByClubId(Integer clubId);
}
