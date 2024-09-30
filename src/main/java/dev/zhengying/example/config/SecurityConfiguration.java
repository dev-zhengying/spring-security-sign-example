package dev.zhengying.example.config;

import dev.zhengying.example.config.sign.SignAuthenticationFilter;
import dev.zhengying.example.config.sign.SignAuthenticationProvider;
import dev.zhengying.example.config.sign.SignClientDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * SecurityConfiguration
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            SignAuthenticationFilter signAuthenticationFilter) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/brand/**").hasAuthority("brand:r")
                        .requestMatchers(HttpMethod.POST, "/brand/**").hasAuthority("brand:w")
                        .requestMatchers(HttpMethod.GET, "/category/**").hasAuthority("category:r")
                        .requestMatchers(HttpMethod.POST, "/category/**").hasAuthority("category:w")
                        .anyRequest().denyAll()
                )
                .addFilterBefore(signAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    SignAuthenticationFilter signAuthenticationFilter(List<AuthenticationProvider> authenticationProviders) {
        return new SignAuthenticationFilter(new ProviderManager(authenticationProviders));
    }

    @Bean
    AuthenticationProvider authenticationProvider(SignClientDetailsService signClientDetailsService) {
        return new SignAuthenticationProvider(signClientDetailsService);
    }
}
