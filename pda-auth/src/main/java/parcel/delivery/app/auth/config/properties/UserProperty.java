package parcel.delivery.app.auth.config.properties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;
import parcel.delivery.app.common.security.core.UserRole;

@Data
@Validated
public final class UserProperty {
    @EqualsAndHashCode.Include
    @Email
    private final @NotNull String username;
    @Password
    @ToString.Exclude
    private final String password;
    private final UserRole role;
    private final String firstName;
    private final String lastName;

    public UserProperty(@NotNull String username, @Password String password, UserRole role, @NotBlank String firstName, @NotBlank String lastName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
