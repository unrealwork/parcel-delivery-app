package parcel.delivery.app.common.security.jwt;


import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;

import java.util.Set;

public record JwtAuthData(UserType userType, Set<RolePrivilege> privileges) {
}
