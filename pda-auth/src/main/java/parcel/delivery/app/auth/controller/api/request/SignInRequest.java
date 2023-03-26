package parcel.delivery.app.auth.controller.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;

@Validated
@Schema(name = "Sign in request",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        description = "Data required for signing in the app")
public record SignInRequest(
        @NotBlank
        @Email
        @Schema(description = "Email of the user specified during registration or provided by administrator",
                example = "john@doe.com"
        )
        String clientId,
        @Password
        @Schema(description = "Password of the user specified during registration or provided by administrator",
                example = "password123"
        )
        String secretKey) {
}
