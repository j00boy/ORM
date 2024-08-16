package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.BoardImage;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Integer> {

    /**
     * boardId를 가진 BoardImage를 List로 반환합니다.
     * @param boardId
     * @return
     */
    List<BoardImage> findByBoardId(int boardId);

    /**
     * boardId를 가진 BoardImage들을 삭제합니다.
     * @param boardId
     */
    void deleteByBoardId(Integer boardId);

    /**
     * imageSrc를 가진 BoardImage를 삭제합니다.
     * @param imageSrc
     */
    void deleteByImageSrc(String imageSrc);
}
