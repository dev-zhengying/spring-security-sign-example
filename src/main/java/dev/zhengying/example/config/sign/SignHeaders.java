package dev.zhengying.example.config.sign;

/**
 * SignHeaders
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
record SignHeaders(String appid, String nonce, String timestamp, String sign) {
}
