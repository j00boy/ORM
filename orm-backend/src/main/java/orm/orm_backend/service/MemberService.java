package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import orm.orm_backend.entity.Member;
import orm.orm_backend.repository.MemberRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // UserId에 해당하는 클럽 반환
    public Set<Integer> getClubs(Integer userId) {
        Optional<Member> members = memberRepository.findByUserId(userId);
        Set<Integer> result = new HashSet<>();
        if (members.isPresent()) {
            List<Member> memberList = members.stream().toList();
            for (Member m : memberList) {
                result.add(m.getClub().getId());
            }
        }
        return result;
    }

    public Page<Member> getPageableMembers(Pageable pageable, Integer userId) {
        return memberRepository.findByUserId(pageable, userId);
    }
}
