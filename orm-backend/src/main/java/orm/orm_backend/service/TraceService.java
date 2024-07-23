package orm.orm_backend.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.repository.TraceImageRepository;
import orm.orm_backend.repository.TraceRepository;
import orm.orm_backend.util.ImageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TraceService {

    private final ImageUtil imageUtil;

    private final UserService userService;

    private final TraceRepository traceRepository;
    private final TraceImageRepository traceImageRepository;

    public TraceDto createTrace(TraceRequestDto creationRequestDto, Integer userId) {
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
    public TraceDto updateTrace(Integer traceeId, TraceRequestDto traceRequestDto, Integer userId) {
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

    @Transactional
    public void completeMeasure(Integer userId, TraceDto traceDto) {
        Trace trace = traceRepository.findById(traceDto.getId()).orElseThrow();
        if (!trace.isOwner(userId)) {
            throw new UnAuthorizedException();
        }
        trace.completeMeasure(traceDto);
    }

    public void updateTraceImages(Integer userId, Integer traceId, List<MultipartFile> images) {
        Trace trace = traceRepository.findById(traceId).orElseThrow();
        if (!trace.isOwner(userId)) {
            throw new UnAuthorizedException();
        }

        List<TraceImage> oldImages = traceImageRepository.findByTraceId(traceId);
        traceImageRepository.deleteAll(oldImages);

        // TODO: 실제 파일 삭제하는 로직 추가

        String path = "/trace/" + traceId;
        List<TraceImage> traceImages = images.stream()
                .map(image -> imageUtil.saveImage(image, path)).map(TraceImage::new).toList();
        traceImageRepository.saveAll(traceImages);
    }
}
