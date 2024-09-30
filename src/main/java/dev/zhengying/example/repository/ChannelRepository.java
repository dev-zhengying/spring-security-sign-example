package dev.zhengying.example.repository;

import dev.zhengying.example.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * ChannelRepository
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    Optional<Channel> findByAppid(String appid);
}
