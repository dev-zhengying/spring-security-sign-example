package dev.zhengying.example.config.sign;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * SignAuthenticationFilter
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
@SuppressWarnings({"NullableProblems", "unused", "RedundantThrows"})
@RequiredArgsConstructor
public class SignAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint = new SignAuthenticationEntryPoint();

    private final AuthenticationConverter authenticationConverter = new SignAuthenticationConverter();

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Authentication authRequest = this.authenticationConverter.convert(request);
            if (authRequest == null) {
                this.logger.trace("Did not process authentication request since failed to find " +
                        "appid and sign in header");
                filterChain.doFilter(request, response);
                return;
            }
            Authentication authResult = this.authenticationManager.authenticate(authRequest);
            onSuccessfulAuthentication(request, response, authResult);
        } catch (InternalAuthenticationServiceException failed) {
            this.logger.error("An internal error occurred while trying to authenticate the user.", failed);
            onUnsuccessfulAuthentication(request, response, failed);
            return;
        } catch (AuthenticationException ex) {
            // Authentication failed
            onUnsuccessfulAuthentication(request, response, ex);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authResult) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
    }

    private void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        this.logger.trace("Failed to process authentication request", failed);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        this.authenticationEntryPoint.commence(request, response, failed);
    }
}
