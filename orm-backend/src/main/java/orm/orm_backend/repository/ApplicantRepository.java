package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Applicant;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    List<Applicant> findByUserId(Integer userId);
    List<Applicant> findByClubId(Integer clubId);

    void deleteByUserIdAndClubId(Integer userId, Integer clubId);
}
