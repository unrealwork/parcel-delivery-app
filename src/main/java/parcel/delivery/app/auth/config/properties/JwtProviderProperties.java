package parcel.delivery.app.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties(prefix = "application.security.jwt.provider")
public record JwtProviderProperties(String secretKey,
                                    Duration expirationDuration) {
}
