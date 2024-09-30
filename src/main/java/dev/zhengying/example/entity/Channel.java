package dev.zhengying.example.entity;

import dev.zhengying.example.config.sign.SignClientDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * Channel
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
@Getter
@Setter
@Entity
public class Channel implements SignClientDetails, Serializable {
    @Serial
    private static final long serialVersionUID = -2265795294030218539L;
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private String appid;
    private String secret;
    private String permission;
}
