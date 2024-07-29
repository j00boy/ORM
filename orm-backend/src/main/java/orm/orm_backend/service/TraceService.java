package orm.orm_backend.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.TraceCoordinateDto;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.repository.TraceCoordinateRepository;
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
    private final MountainService mountainService;
    private final TrailService trailService;

    private final TraceRepository traceRepository;
    private final TraceImageRepository traceImageRepository;
    private final TraceCoordinateRepository traceCoordinateRepository;

    public TraceDto createTrace(TraceRequestDto creationRequestDto, Integer userId) {
        // mountain, trail 객체 받는 로직 추후 추가
//        Mountain mountain = mountainService.get()
//        Trail trail = trailService.get();
        Mountain mountain = mountainService.getMountainById(creationRequestDto.getMountainId());
        Trail trail = trailService.getTrailEntityById(creationRequestDto.getTrailId());

        User user = userService.findUserById(userId);

        Trace trace = Trace.builder()
                .traceRequestDto(creationRequestDto)
                .mountain(mountain)
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
        Mountain mountain = mountainService.getMountainById(traceRequestDto.getMountainId());
        Trail trail = trailService.getTrailEntityById(traceRequestDto.getTrailId());
        trace.update(traceRequestDto, mountain, trail);
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
        List<TraceCoordinateDto> coordinateDtos = traceDto.getCoordinates();
        List<TraceCoordinate> traceCoordinates = coordinateDtos.stream()
                .map(coordinateDto -> new TraceCoordinate(coordinateDto, trace)).toList();
        traceCoordinateRepository.saveAll(traceCoordinates);
    }

    @Transactional
    public void updateTraceImages(Integer userId, Integer traceId, List<MultipartFile> images) {
        Trace trace = traceRepository.findById(traceId).orElseThrow();
        if (!trace.isOwner(userId)) {
            throw new UnAuthorizedException();
        }

        List<TraceImage> oldImages = traceImageRepository.findByTraceId(traceId);
        traceImageRepository.deleteAll(oldImages);

        List<String> imageFileNames = oldImages.stream().map(TraceImage::getImageSrc).toList();
        imageUtil.deleteImages(imageFileNames);

        String path = "/trace/" + traceId + "/";
        List<TraceImage> traceImages = images.stream()
                .map(image -> imageUtil.saveImage(image, path)).map(TraceImage::new).toList();
        traceImageRepository.saveAll(traceImages);
    }
}
