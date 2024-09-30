package dev.zhengying.example.config.sign;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.AuthenticationConverter;

/**
 * SignAuthenticationConverter
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
final class SignAuthenticationConverter implements AuthenticationConverter {

    @Override
    public SignAuthenticationToken convert(HttpServletRequest request) {
        String appid = request.getHeader("appid");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        if (appid == null || sign == null) {
            return null;
        }
        SignHeaders headers = new SignHeaders(appid, nonce, timestamp, sign);
        return new SignAuthenticationToken(headers);
    }
}
