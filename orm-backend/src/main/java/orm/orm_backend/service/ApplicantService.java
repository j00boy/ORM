package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.entity.Applicant;
import orm.orm_backend.entity.Member;
import orm.orm_backend.repository.ApplicantRepository;
import orm.orm_backend.repository.MemberRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    // UserId에 해당하는 클럽 반환
    public Set<Integer> getApplicants(Integer userId) {
        Optional<Applicant> applicants = applicantRepository.findByUserId(userId);
        Set<Integer> result = new HashSet<>();
        if (applicants.isPresent()) {
            List<Applicant> applicantList = applicants.stream().toList();
            for (Applicant a : applicantList) {
                result.add(a.getClub().getId());
            }
        }
        return result;
    }

    // Club에 가입 신청한 인원
    public List<Applicant> getApplicantsInClub(Integer clubId) {
        return applicantRepository.findByClubId(clubId).stream().toList();
    }
}
