package parcel.delivery.app.auth.controller.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;

@Builder
@Schema(description = "Request for registration in the app",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        name = "Sign up request")
public record SignUpRequest(
        @NotBlank
        @Email
        @Schema(description = "Email of the user",
                example = "john@doe.com",
                name = "clientId"
        )
        String clientId,
        @NotBlank
        @Schema(description = "First Name of the user",
                example = "John"
        )
        String firstName,
        @NotBlank
        @Schema(description = "Last Name of the user",
                example = "Doe"
        )
        String lastName,
        @NotBlank
        @Schema(description = "Phone number of the user.",
                example = "+12312435678"
        )
        String phone,

        @Schema(description = "Password for signing in in the app",
                example = "password123",
                minimum = "8", maximum = "20"
        )
        @Password String password) {
}
