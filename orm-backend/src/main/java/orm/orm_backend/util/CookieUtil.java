package orm.orm_backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * boardId에 해당하는 쿠키가 유효한지 확인
     * @param boardId
     * @param cookies
     * @return
     */
    public static boolean checkCookie(String cookiePrefix, Integer boardId, Cookie[] cookies) {
        String cookieName = cookiePrefix + "-" + boardId;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void setCookie(String cookiePrefix, String cookiePath, HttpServletResponse response, Integer boardId) {
        Cookie cookie = new Cookie(cookiePrefix + "-" + boardId, "true");
        cookie.setMaxAge(60 * 5);   // 5분
        cookie.setPath(cookiePath);    // 해당 경로에서만
        response.addCookie(cookie);
    }
}
