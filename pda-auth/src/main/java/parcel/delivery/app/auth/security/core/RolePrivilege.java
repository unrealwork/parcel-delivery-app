package parcel.delivery.app.auth.security.core;

import org.springframework.security.core.GrantedAuthority;

public enum RolePrivilege implements GrantedAuthority {
    CREATE_COURIER_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
