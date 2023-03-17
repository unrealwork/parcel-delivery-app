package parcel.delivery.app.auth.controller.api.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;

@Builder
public record SignUpRequest(@NotBlank @Email String clientId,
                            @NotBlank String firstName,
                            @NotBlank String lastName,
                            @NotBlank String phone,
                            @Password String password) {
}
