package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orm.orm_backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
