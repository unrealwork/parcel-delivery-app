package parcel.delivery.app.auth.controller.api.response;



import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.Collection;

public record AuthData(String clientId, UserRole userRole, Collection<RolePrivilege> privileges) {
}
