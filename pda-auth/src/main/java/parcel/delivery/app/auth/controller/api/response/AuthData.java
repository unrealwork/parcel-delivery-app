package parcel.delivery.app.auth.controller.api.response;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.Collection;


@Schema(name = "Authorization data",
        description = "Contains authorization data from provided JWT token"
)
public record AuthData(
        @Email
        @Schema(description = "Email of the authenticated user",
                example = "john@doe.com"
        )
        String clientId,
        @Schema(description = "Role of the user in the app",
                example = "ADMIN"
        )
        @NotNull UserRole role,
        @ArraySchema(schema = @Schema(description = "Privilege of the user", example = "CREATE_ORDER"),
                uniqueItems = true)
        Collection<RolePrivilege> privileges) {
}
