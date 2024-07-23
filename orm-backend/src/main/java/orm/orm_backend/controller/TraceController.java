package orm.orm_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.service.TraceService;
import orm.orm_backend.util.JwtUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trace")
public class TraceController {

    @Value("${orm.header.auth}")
    private String HEADER_AUTH;

    private final JwtUtil jwtUtil;

    private final TraceService traceService;

    @PostMapping("/create")
    public ResponseEntity<TraceDto> createTrace(HttpServletRequest request, TraceRequestDto traceRequestDto) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        TraceDto traceDto = traceService.createTrace(traceRequestDto, userId);
        HttpHeaders headers = jwtUtil.createTokenHeaders(userId);
        return ResponseEntity.created(URI.create("/trace/" + traceDto.getId()))
                .headers(headers).body(traceDto);
    }

    @PatchMapping("/update")
    public ResponseEntity<TraceDto> updateBeforeMeasure(HttpServletRequest request,
                                                        TraceRequestDto traceRequestDto, Integer traceId) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        return ResponseEntity.ok(traceService.updateTrace(traceId, traceRequestDto, userId));
    }

    @DeleteMapping("/{traceId}")
    public ResponseEntity<Void> deleteTrace(HttpServletRequest request, @PathVariable Integer traceId) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        traceService.deleteTrace(traceId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/measure-complete")
    public ResponseEntity<Void> completeMeasure(HttpServletRequest request, TraceDto traceDto) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        traceService.completeMeasure(userId, traceDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/images/{traceId}")
    public ResponseEntity<Void> updateTraceImages(HttpServletRequest request, List<MultipartFile> images, Integer traceId) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        traceService.updateTraceImages(userId, traceId, images);
        return ResponseEntity.ok().build();
    }
}
