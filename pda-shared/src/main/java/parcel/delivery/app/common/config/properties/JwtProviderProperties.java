package parcel.delivery.app.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties(prefix = "application.security.jwt.provider")
@Getter
@Setter
public final class JwtProviderProperties {
    private boolean enabled = true;
    @Value("${JWT_ALLOW_SPECIAL_KEY: false}")
    private boolean specialKeyAllowed = false;
    private String secretKey;
    private Duration expirationDuration = Duration.ofHours(1);
}
