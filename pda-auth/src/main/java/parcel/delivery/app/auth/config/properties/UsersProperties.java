package parcel.delivery.app.auth.config.properties;

import jakarta.validation.Valid;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@ConfigurationProperties(prefix = "application")
@Validated
public record UsersProperties(@Valid Set<UserProperty> users) {
}

