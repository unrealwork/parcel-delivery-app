package parcel.delivery.app.common.security.jwt;


import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;

import java.util.Collection;

public record JwtAuthData(UserType userType, Collection<RolePrivilege> privileges) {
}
