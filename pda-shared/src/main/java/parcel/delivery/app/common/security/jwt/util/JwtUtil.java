package parcel.delivery.app.common.security.jwt.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.common.security.jwt.JwtToken;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class JwtUtil {
    public static Authentication authenticationFromToken(JwtToken jwtToken) {
        return new UsernamePasswordAuthenticationToken(jwtToken.clientId(), "", jwtToken.authorities());
    }

    public static JwtToken tokenFromAuthentication(Authentication authentication) {
        JwtToken.JwtTokenBuilder tokenBuilder = JwtToken.builder()
                .clientId(authentication.getName());
        boolean isRoleFound = false;
        Set<RolePrivilege> privilegeSet = new HashSet<>();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            final UserType userType = UserType.fromAuthority(authority);
            if (userType != null) {
                if (isRoleFound) {
                    throw new BadCredentialsException("User has several roles");
                } else {
                    isRoleFound = true;
                    tokenBuilder.userType(userType);
                }
            } else {
                RolePrivilege privilege = RolePrivilege.valueOf(authority);
                privilegeSet.add(privilege);
            }
        }
        if (!isRoleFound) {
            throw new BadCredentialsException("User should have at least one role");
        }
        return tokenBuilder.privileges(privilegeSet)
                .build();
    }
}
