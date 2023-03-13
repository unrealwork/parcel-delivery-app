package parcel.delivery.app.auth.security.jwt;

import lombok.Builder;
import parcel.delivery.app.auth.security.core.RolePrivilege;
import parcel.delivery.app.auth.security.core.UserType;

import java.time.Instant;
import java.util.Collection;

@Builder
public record JwtToken(String clientId, Instant issuedAt, Instant expiresAt,
                       UserType userType,
                       Collection<RolePrivilege> authorities) {
}
