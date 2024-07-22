package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.request.TraceCreationRequestDto;
import orm.orm_backend.dto.response.TraceResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trace;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.User;
import orm.orm_backend.repository.TraceRepository;
import orm.orm_backend.util.JwtUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TraceService {

    private final UserService userService;

    private final TraceRepository traceRepository;

    public TraceResponseDto createTrace(TraceCreationRequestDto creationRequestDto, Integer userId) {
        // mountain, trail 객체 받는 로직 추후 추가
//        Mountain mountain = mountainService.get()
//        Trail trail = trailService.get();
        Mountain mountain = null;
        Trail trail = null;

        User user = userService.findUserById(userId);

        Trace trace = Trace.builder()
                .traceCreationRequestDto(creationRequestDto)
                .trail(trail)
                .user(user)
                .build();
        Trace savedTrace = traceRepository.save(trace);
        return savedTrace.toResponseDto();
    }
}
