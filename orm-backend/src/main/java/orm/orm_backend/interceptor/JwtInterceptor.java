package orm.orm_backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.util.JwtUtil;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final String HEADER_AUTH = "Authorization";
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HEADER_AUTH);
        log.info("token");
        if (token != null && jwtUtil.checkToken(token)) {
            return true;
        }
        throw new UnAuthorizedException();
    }
}
