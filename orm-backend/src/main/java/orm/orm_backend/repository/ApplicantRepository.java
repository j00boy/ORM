package orm.orm_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import orm.orm_backend.entity.Applicant;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Member;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    Optional<Applicant> findByClubIdAndUserId(Integer clubId, Integer userId);
}
