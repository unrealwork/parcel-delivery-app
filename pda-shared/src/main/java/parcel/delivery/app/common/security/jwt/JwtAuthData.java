package parcel.delivery.app.common.security.jwt;


import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.Set;

public record JwtAuthData(UserRole userRole, Set<RolePrivilege> privileges) {
}
