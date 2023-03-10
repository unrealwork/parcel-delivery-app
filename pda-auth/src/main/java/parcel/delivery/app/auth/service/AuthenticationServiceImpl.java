package parcel.delivery.app.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.api.models.request.SignInRequest;
import parcel.delivery.app.auth.api.models.request.SignUpRequest;
import parcel.delivery.app.auth.api.models.response.AuthData;
import parcel.delivery.app.auth.api.models.response.SignInResponse;
import parcel.delivery.app.auth.dto.RoleDto;
import parcel.delivery.app.auth.dto.UserDto;
import parcel.delivery.app.auth.security.exceptions.UserAlreadyExistException;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.common.security.jwt.JwtProvider;
import parcel.delivery.app.common.security.jwt.JwtToken;
import parcel.delivery.app.common.security.jwt.util.JwtUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
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
    public SignInResponse signIn(SignInRequest signInRequest) {
        final String clientId = signInRequest.clientId();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(clientId);
        Authentication authentication = authenticateUser(userDetails, signInRequest);
        JwtToken jwtToken = JwtUtil.tokenFromAuthentication(authentication);
        return new SignInResponse(jwtProvider.generate(jwtToken));

    }

    @Override
    public AuthData me() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        JwtToken jwtToken = JwtUtil.tokenFromAuthentication(authentication);
        return new AuthData(jwtToken.clientId(), jwtToken.userType(), jwtToken.privileges());
    }

    private Authentication authenticateUser(final UserDetails userDetails, SignInRequest signInRequest) {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), signInRequest.secretKey(), userDetails.getAuthorities());
        return authenticationManager.authenticate(authentication);
    }
}
