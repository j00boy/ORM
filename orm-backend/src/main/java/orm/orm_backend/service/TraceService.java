package orm.orm_backend.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.dto.response.TraceResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trace;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.User;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.repository.TraceRepository;

@Service
@RequiredArgsConstructor
public class TraceService {

    private final UserService userService;

    private final TraceRepository traceRepository;

    public TraceResponseDto createTrace(TraceRequestDto creationRequestDto, Integer userId) {
        // mountain, trail 객체 받는 로직 추후 추가
//        Mountain mountain = mountainService.get()
//        Trail trail = trailService.get();
        Mountain mountain = null;
        Trail trail = null;

        User user = userService.findUserById(userId);

        Trace trace = Trace.builder()
                .traceRequestDto(creationRequestDto)
                .trail(trail)
                .user(user)
                .build();
        Trace savedTrace = traceRepository.save(trace);
        return savedTrace.toResponseDto();
    }

    @Transactional
    public TraceResponseDto updateTrace(Integer traceeId, TraceRequestDto traceRequestDto, Integer userId) {
        Trace trace = traceRepository.findById(traceeId).orElseThrow(NoResultException::new);
        if (!trace.isOwner(userId)) {
            throw new UnAuthorizedException();
        }
        trace.update(traceRequestDto);
        return trace.toResponseDto();
    }

    public void deleteTrace(Integer traceId, Integer userId) {
        Trace trace = traceRepository.findById(traceId).orElseThrow(NoResultException::new);
        if (!trace.isOwner(userId)) {
            throw new UnAuthorizedException();
        }
        traceRepository.delete(trace);
    }
}
