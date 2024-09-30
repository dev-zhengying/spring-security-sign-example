package dev.zhengying.example.config.sign;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SignAuthenticationProvider
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
public class SignAuthenticationProvider implements AuthenticationProvider {

    private final SignClientDetailsService signClientDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SignAuthenticationToken.class, authentication,
                "Only SignAuthenticationToken is supported");
        SignAuthenticationToken authenticationToken = (SignAuthenticationToken) authentication;
        SignHeaders headers = authenticationToken.getHeaders();
        String appid = headers.appid();
        SignClientDetails client;
        try {
            client = signClientDetailsService.loadClientByAppid(appid);
        } catch (UsernameNotFoundException ex) {
            log.debug("Failed to find client '{}'", appid);
            throw ex;
        }
        Assert.notNull(client, "loadClientByAppid returned null - a violation of the interface contract");
        this.additionalAuthenticationChecks(client, headers);
        Object principal = client.getAppid();
        return this.createSuccessAuthentication(principal, authenticationToken, client);
    }

    private void additionalAuthenticationChecks(SignClientDetails client,
                                                SignHeaders headers) throws AuthenticationException {
        String sign = this.reSign(headers, client.getSecret());
        if (!Objects.equals(headers.sign(), sign)) {
            throw new BadCredentialsException("Failed to verify sign");
        }
    }

    private String reSign(SignHeaders headers, String secret) {
        String format = "appid=" + headers.appid() +
                "&nonce=" + headers.nonce() +
                "&timestamp=" + headers.timestamp();
        byte[] data = format.getBytes();
        byte[] key = secret.getBytes();
        HMac hmac = new HMac(HmacAlgorithm.HmacMD5, key);
        return hmac.digestHex(data);
    }

    private Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                       SignClientDetails client) {
        Collection<? extends GrantedAuthority> authorities;
        String permission = client.getPermission();
        if (StringUtils.isEmpty(permission)) {
            authorities = Collections.emptySet();
        } else {
            authorities = Arrays.stream(permission.split(","))
                    .map(e -> (GrantedAuthority) () -> e)
                    .collect(Collectors.toSet());
        }
        SignAuthenticationToken result = new SignAuthenticationToken(principal, authorities);
        result.setDetails(client);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SignAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
