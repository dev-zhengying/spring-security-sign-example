package dev.zhengying.example.config.sign;

/**
 * SignClientDetails
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
public interface SignClientDetails {

    String getAppid();

    String getSecret();

    String getPermission();
}
