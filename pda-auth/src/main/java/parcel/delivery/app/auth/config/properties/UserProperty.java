package parcel.delivery.app.auth.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.Objects;

@Validated
@Getter
public final class UserProperty {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserProperty that = (UserProperty) o;
        return Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
