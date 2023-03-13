package parcel.delivery.app.auth.security.jwt;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import parcel.delivery.app.auth.security.core.RolePrivilege;
import parcel.delivery.app.auth.security.core.UserType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Builder
public record JwtToken(String clientId, Instant issuedAt, Instant expiresAt,
                       UserType userType,
                       Collection<RolePrivilege> privileges) {


    public Collection<GrantedAuthority> authorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>(1 + privileges.size());
        authorityList.add(userType);
        authorityList.addAll(privileges);
        return Collections.unmodifiableList(authorityList);
    }
}
