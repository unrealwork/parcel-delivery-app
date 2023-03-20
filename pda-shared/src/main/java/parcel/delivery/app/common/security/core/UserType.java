package parcel.delivery.app.common.security.core;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static parcel.delivery.app.common.security.core.RolePrivilege.ASSIGN_COURIER;
import static parcel.delivery.app.common.security.core.RolePrivilege.BASIC;
import static parcel.delivery.app.common.security.core.RolePrivilege.CANCEL_ORDER;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_DESTINATION;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_ORDER_STATUS;
import static parcel.delivery.app.common.security.core.RolePrivilege.CREATE_COURIER_USER;
import static parcel.delivery.app.common.security.core.RolePrivilege.CREATE_ORDER;
import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_COURIERS;
import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_DELIVERY_DETAILS;
import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_ORDERS;

public enum UserType implements GrantedAuthority {
    USER(BASIC,
            VIEW_ORDERS,
            CREATE_ORDER,
            CANCEL_ORDER,
            CHANGE_DESTINATION,
            VIEW_DELIVERY_DETAILS),
    ADMIN(BASIC,
            CREATE_COURIER_USER,
            CHANGE_ORDER_STATUS,
            VIEW_ORDERS,
            VIEW_COURIERS,
            ASSIGN_COURIER),
    COURIER(BASIC,
            CHANGE_ORDER_STATUS,
            VIEW_ORDERS,
            VIEW_DELIVERY_DETAILS);

    public static final String ROLE_PREFIX = "ROLE_";
    private final Set<RolePrivilege> priviligesSet;

    UserType(RolePrivilege... privileges) {
        this.priviligesSet = Set.of(privileges);
    }


    @Override
    public String getAuthority() {
        return ROLE_PREFIX + name();
    }


    public Collection<RolePrivilege> priviliges() {
        return this.priviligesSet;
    }

    public static UserType fromAuthority(@NonNull String authority) {
        if (authority.startsWith(ROLE_PREFIX)) {
            try {
                return UserType.valueOf(authority.substring(ROLE_PREFIX.length()));
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException("Unknown role for authentication:  " + authority, e);
            }
        }
        throw new BadCredentialsException("ROLE authority should start with " + ROLE_PREFIX);
    }

    public static UserType fromAuthority(@NonNull GrantedAuthority grantedAuthority) {
        return fromAuthority(grantedAuthority.getAuthority());
    }
}
