package parcel.delivery.app.auth.controller.api.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(accessMode = Schema.AccessMode.READ_ONLY,
        name = "Access data",
        description = "Data to access other API resources."
)
public record SignInResponse(
        @Schema(description = "JWT token for accessing other resources of API. Should be specified as bearer token in Authorization HTTP header.")
        String accessToken) {
}
