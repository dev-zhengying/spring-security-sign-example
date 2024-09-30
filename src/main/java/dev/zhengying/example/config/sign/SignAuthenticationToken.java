package dev.zhengying.example.config.sign;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * SignAuthenticationToken
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
@SuppressWarnings("serial")
@Getter
class SignAuthenticationToken extends AbstractAuthenticationToken {

    private final SignHeaders headers;
    private final Object principal;

    SignAuthenticationToken(SignHeaders headers) {
        super(null);
        this.headers = headers;
        this.principal = null;
        setAuthenticated(false);
    }

    SignAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.headers = null;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
