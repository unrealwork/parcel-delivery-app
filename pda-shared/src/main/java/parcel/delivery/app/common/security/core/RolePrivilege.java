package parcel.delivery.app.common.security.core;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

public enum RolePrivilege implements GrantedAuthority {
    BASIC, CREATE_COURIER_USER, VIEW_ORDERS, CREATE_ORDER, CHANGE_ORDER_STATUS, CANCEL_ORDER, CHANGE_DESTINATION, VIEW_DELIVERY_DETAILS, ASSIGN_COURIER, VIEW_COURIERS, TRACK_DELIVERY;

    @Override
    public String getAuthority() {
        return name();
    }

    public static RolePrivilege fromAuthority(@NonNull String authority) {
        try {
            return valueOf(authority);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Unable to parse RolePrivilege", e);
        }
    }

    public static RolePrivilege fromAuthority(@NonNull GrantedAuthority grantedAuthority) {
        return fromAuthority(grantedAuthority.getAuthority());
    }
}
