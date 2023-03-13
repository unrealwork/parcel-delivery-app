package parcel.delivery.app.common.security.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

public final class PdaUserDetails extends User {

    @Serial
    private static final long serialVersionUID = -5341192055877669849L;

    public PdaUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static PdaUserDetails create(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        return new PdaUserDetails(username, password, authorities);
    }
}
