package parcel.delivery.app.common.mapper.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties(prefix = "application.security.jwt.provider")
@Getter
@Setter
public final class JwtProviderProperties {
    private boolean enabled = true;
    private String secretKey;
    private Duration expirationDuration = Duration.ofHours(1);
}