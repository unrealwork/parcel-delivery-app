package parcel.delivery.app.auth.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;
import parcel.delivery.app.common.security.core.UserRole;

@Validated
@Getter
@EqualsAndHashCode
public final class UserProperty {

    @EqualsAndHashCode.Include
    private final @NotNull String username;
    @Password
    private final String password;
    private final UserRole role;
    private final @NotBlank String firstName;
    private final @NotBlank String lastName;

    public UserProperty(@NotNull String username, @Password String password, UserRole role,
                        @NotBlank String firstName, @NotBlank String lastName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
