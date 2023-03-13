package parcel.delivery.app.auth.security.jwt;

import parcel.delivery.app.auth.security.core.RolePrivilege;
import parcel.delivery.app.auth.security.core.UserType;

import java.util.Collection;

public record JwtAuthData(UserType userType, Collection<RolePrivilege> privileges) {
}
