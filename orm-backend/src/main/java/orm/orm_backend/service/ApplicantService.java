package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.request.MemberRequestDto;
import orm.orm_backend.entity.Applicant;
import orm.orm_backend.repository.ApplicantRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    // UserId에 해당하는 클럽 반환
    public Set<Integer> getApplicants(Integer userId) {
        List<Applicant> applicants = applicantRepository.findByUserId(userId);
        Set<Integer> result = new HashSet<>();
        applicants.stream()
                .filter(applicant -> applicant.getClub() != null)
                .map(applicant -> applicant.getClub().getId()).forEach(result::add);
        return result;
    }

    // Club에 가입 신청한 인원
    public List<Applicant> getApplicantsInClub(Integer clubId) {
        return applicantRepository.findByClubId(clubId);
    }

    // Applicant 저장
    public Applicant saveApplicant(Applicant applicant) {
        return applicantRepository.save(applicant);
    }

    // Applicant 삭제
    public void deleteApplicant(MemberRequestDto memberRequestDto) {
        applicantRepository.deleteByUserIdAndClubId(memberRequestDto.getUserId(), memberRequestDto.getClubId());
    }

    // Applicant 목록에 이미 존재하는가?
    public Boolean isContained(Integer userId, Integer clubId) {
        return applicantRepository.existsByUserIdAndClubId(userId, clubId);
    }

    public void deleteOrphanApplicants() {
        applicantRepository.deleteByClubIsNull();
    }
}
