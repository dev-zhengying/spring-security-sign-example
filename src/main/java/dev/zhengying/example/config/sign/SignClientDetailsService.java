package dev.zhengying.example.config.sign;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * SignClientDetailsService
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
public interface SignClientDetailsService {

    SignClientDetails loadClientByAppid(String appid) throws UsernameNotFoundException;
}
