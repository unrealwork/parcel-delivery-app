package parcel.delivery.app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;

import java.io.Serializable;
import java.util.Collection;

/**
 * A DTO for the {@link parcel.delivery.app.auth.domain.User} entity
 */
@Builder
public record UserDto(Long id, @Email String clientId, @Password String password, @NotBlank String firstName,
                      @NotNull String lastName, @NotNull Collection<RoleDto> roles) implements Serializable {
}
