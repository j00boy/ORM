package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
