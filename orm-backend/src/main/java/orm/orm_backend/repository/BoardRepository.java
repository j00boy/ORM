package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import orm.orm_backend.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    /**
     * 특정 clubId에 속한 게시글들을 모두 조회합니다.
     * @param clubId
     * @return
     */
    List<Board> findByClubId(Integer clubId);

    /**
     * 조회수 증가하는 메서드
     * @param boardId
     */
    @Modifying
    @Query("update Board b set b.hit = b.hit + 1 where b.id = :boardId")
    void updateHits(Integer boardId);

    void deleteByClubIsNull();
}
