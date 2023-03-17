package parcel.delivery.app.auth.controller.api.response;



import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;

import java.util.Collection;

public record AuthData(String clientId, UserType userType, Collection<RolePrivilege> privileges) {
}
