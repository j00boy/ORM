package orm.orm_backend.batch;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import orm.orm_backend.service.ApplicantService;
import orm.orm_backend.service.BoardService;
import orm.orm_backend.service.MemberService;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchService {

    private final MemberService memberService;
    private final ApplicantService applicantService;
    private final BoardService boardService;

    @Scheduled(cron = "${schedule.cron.delete-club}")
    @Transactional
    public void deleteClubData() {
        applicantService.deleteOrphanApplicants();
        memberService.deleteOrphanMembers();
        boardService.deleteOrphanBoards();
    }
}
