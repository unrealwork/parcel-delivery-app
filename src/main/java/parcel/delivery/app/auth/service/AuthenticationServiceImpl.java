package parcel.delivery.app.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.api.models.request.SignInRequest;
import parcel.delivery.app.auth.api.models.request.SignUpRequest;
import parcel.delivery.app.auth.api.models.response.AuthData;
import parcel.delivery.app.auth.api.models.response.SignInResult;
import parcel.delivery.app.auth.dto.RoleDto;
import parcel.delivery.app.auth.dto.UserDto;
import parcel.delivery.app.auth.security.core.RolePrivilege;
import parcel.delivery.app.auth.security.core.UserType;
import parcel.delivery.app.auth.security.exceptions.UserAlreadyExistException;
import parcel.delivery.app.auth.security.jwt.JwtProvider;
import parcel.delivery.app.auth.security.jwt.JwtToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public final class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void signUp(SignUpRequest signUpRequestDto, UserType userType) {
        final String clientId = signUpRequestDto.clientId();
        if (userService.findByClientId(clientId)
                .isPresent()) {
            throw new UserAlreadyExistException("User " + clientId + " is already exist");
        }
        final RoleDto userRole = roleService.findRoleByAuthority(userType.getAuthority())
                .orElseThrow(() -> new IllegalStateException("Role is not presented in DB"));
        final UserDto user = UserDto.builder()
                .clientId(signUpRequestDto.clientId())
                .password(signUpRequestDto.password())
                .firstName(signUpRequestDto.firstName())
                .lastName(signUpRequestDto.lastName())
                .roles(List.of(userRole))
                .build();
        userService.save(user);
    }

    @Override
    public SignInResult signIn(SignInRequest signInRequest) {
        final String clientId = signInRequest.clientId();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(clientId);
        Authentication authentication = authenticateUser(userDetails, signInRequest);
        SecurityContextHolder.setContext(authenticatedContext(authentication));
        JwtToken jwtToken = createFromAuthentication(authentication);
        return new SignInResult(jwtProvider.generate(jwtToken));

    }

    @Override
    public AuthData me() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        JwtToken jwtToken = createFromAuthentication(authentication);
        return new AuthData(jwtToken.clientId(), jwtToken.userType(), jwtToken.authorities());
    }

    private JwtToken createFromAuthentication(Authentication authentication) {
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
        return tokenBuilder.authorities(privilegeSet)
                .build();
    }

    private SecurityContext authenticatedContext(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

    private Authentication authenticateUser(final UserDetails userDetails, SignInRequest signInRequest) {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), signInRequest.secretKey(), userDetails.getAuthorities());
        return authenticationManager.authenticate(authentication);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtProvider.validate(token);
    }

    @Override
    public String refreshToken(String refreshToken) {
        throw new UnsupportedOperationException();
    }
}
