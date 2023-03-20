package parcel.delivery.app.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.security.jwt.JwtToken;
import parcel.delivery.app.common.security.jwt.util.JwtUtil;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {

    public Authentication authentication() {
        return SecurityContextHolder.getContext()
                .getAuthentication();
    }

    public String username() {
        return authentication().getName();
    }

    public JwtToken jwtToken() {
        return JwtUtil.tokenFromAuthentication(authentication());
    }

    public UserRole role() {
        return jwtToken().userRole();
    }

    public Collection<RolePrivilege> privileges() {
        return jwtToken().privileges();
    }
}
