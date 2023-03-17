package parcel.delivery.app.auth.controller.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import parcel.delivery.app.auth.annotations.validation.constrains.Password;

public record SignInRequest(@NotBlank @Email String clientId,
                            @Password String secretKey) {
}
