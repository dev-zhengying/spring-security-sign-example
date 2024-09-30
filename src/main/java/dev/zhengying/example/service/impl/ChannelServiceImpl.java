package dev.zhengying.example.service.impl;

import dev.zhengying.example.config.sign.SignClientDetails;
import dev.zhengying.example.repository.ChannelRepository;
import dev.zhengying.example.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ChannelServiceImpl
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository repository;

    @Override
    public SignClientDetails loadClientByAppid(String appid) throws UsernameNotFoundException {
        return repository.findByAppid(appid)
                .orElseThrow(() -> new UsernameNotFoundException(appid));
    }
}
