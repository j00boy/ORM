package orm.orm_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUserId(Integer userId);
    Optional<Member> findByClubId(Integer clubId);

    Page<Member> findByUserId(Pageable pageable, Integer userId);

    void deleteByUserIdAndClubId(Integer userId, Integer clubId);
}
