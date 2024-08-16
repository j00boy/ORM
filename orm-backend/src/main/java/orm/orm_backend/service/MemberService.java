package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.entity.Member;
import orm.orm_backend.repository.MemberRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // userId에 해당하는 클럽 반환
    public Set<Integer> getClubs(Integer userId) {
        List<Member> members = memberRepository.findByUserId(userId);
        Set<Integer> result = new HashSet<>();
        members.stream()
                .filter(member -> member.getClub() != null)
                .map(member -> member.getClub().getId()).forEach(result::add);
        return result;
    }

    // userId에 해당하는 member 반환
    public List<Member> getMemberList(Integer userId) {
        return memberRepository.findByUserId(userId);
    }

    // club에 속한 member 반환
    public List<Member> getMembersInClub(Integer clubId) {
        return memberRepository.findByClubId(clubId);
    }

    // Member 저장
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    // Member 삭제
    public void delete(Integer userId, Integer clubId) {
        memberRepository.deleteByUserIdAndClubId(userId, clubId);
    }

    // club 내부에 해당 member가 존재하는지?
    public Boolean isContained(Integer userId, Integer clubId) {
        return memberRepository.existsByUserIdAndClubId(userId, clubId);
    }

    public void deleteOrphanMembers() {
        memberRepository.deleteByClubIsNull();
    }
}
