package parcel.delivery.app.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties(prefix = "application.security.jwt.provider")
@Data
public final class JwtProviderProperties {
    private String secretKey;
    private Duration expirationDuration = Duration.ofMinutes(20);
}
