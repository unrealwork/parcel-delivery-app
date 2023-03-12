package parcel.delivery.app.auth.security.core;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    USER, ADMIN, COURIER;

    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return ROLE_PREFIX + name();
    }

    public static UserType fromAuthority(@NonNull String authority) {
        if (authority.startsWith(ROLE_PREFIX)) {
            return UserType.valueOf(authority.substring(ROLE_PREFIX.length()));
        }
        return null;
    }
}
