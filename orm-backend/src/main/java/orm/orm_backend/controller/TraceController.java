package orm.orm_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.dto.response.TraceResponseDto;
import orm.orm_backend.service.TraceService;
import orm.orm_backend.util.JwtUtil;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trace")
public class TraceController {

    @Value("${orm.header.auth}")
    private String HEADER_AUTH;

    private final JwtUtil jwtUtil;

    private final TraceService traceService;

    @PostMapping("/create")
    public ResponseEntity<TraceResponseDto> createTrace(HttpServletRequest request, TraceRequestDto traceRequestDto) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        TraceResponseDto traceResponseDto = traceService.createTrace(traceRequestDto, userId);
        HttpHeaders headers = jwtUtil.createTokenHeaders(userId);
        return ResponseEntity.created(URI.create("/trace/" + traceResponseDto.getId()))
                .headers(headers).body(traceResponseDto);
    }
}
