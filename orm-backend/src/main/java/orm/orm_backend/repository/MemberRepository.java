package orm.orm_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findByUserId(Integer userId);
    List<Member> findByClubId(Integer clubId);

    void deleteByUserIdAndClubId(Integer userId, Integer clubId);

    Boolean existsByUserIdAndClubId(Integer userId, Integer clubId);
}
