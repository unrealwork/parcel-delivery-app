package parcel.delivery.app.common.security.jwt;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Builder
public record JwtToken(String clientId, Instant issuedAt, Instant expiresAt,
                       UserRole userRole,
                       Set<RolePrivilege> privileges) {


    public Collection<GrantedAuthority> authorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>(1 + privileges.size());
        authorityList.add(userRole);
        authorityList.addAll(privileges);
        return Collections.unmodifiableList(authorityList);
    }
}
