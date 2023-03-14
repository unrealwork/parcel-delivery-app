package parcel.delivery.app.common.security.core;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
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
            try {
                return UserType.valueOf(authority.substring(ROLE_PREFIX.length()));
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException("Unable to parse UserType", e);
            }
        }
        throw new BadCredentialsException("ROLE authority should start with " + ROLE_PREFIX);
    }

    public static UserType fromAuthority(@NonNull GrantedAuthority grantedAuthority) {
        return fromAuthority(grantedAuthority.getAuthority());
    }
}
