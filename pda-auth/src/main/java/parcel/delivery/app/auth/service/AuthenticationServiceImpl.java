package parcel.delivery.app.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.controller.api.request.SignInRequest;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.controller.api.response.AuthData;
import parcel.delivery.app.auth.controller.api.response.SignInResponse;
import parcel.delivery.app.auth.dto.RoleDto;
import parcel.delivery.app.auth.dto.UserDto;
import parcel.delivery.app.auth.security.exceptions.UserAlreadyExistException;
import parcel.delivery.app.common.messaging.Events;
import parcel.delivery.app.common.messaging.events.SignedUpEvent;
import parcel.delivery.app.common.security.core.UserRole;
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

    private final StreamBridge streamBridge;

    @Override
    @Transactional
    public void signUp(SignUpRequest signUpRequestDto, UserRole userType) {
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
        sendUserCreatedEvent(userType, clientId);
    }

    private void sendUserCreatedEvent(UserRole userType, String clientId) {
        SignedUpEvent signedUpEvent = new SignedUpEvent(userType, clientId);
        streamBridge.send(Events.USER_CREATED, signedUpEvent);
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
        return new AuthData(jwtToken.clientId(), jwtToken.userRole(), jwtToken.privileges());
    }

    private Authentication authenticateUser(final UserDetails userDetails, SignInRequest signInRequest) {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), signInRequest.secretKey(), userDetails.getAuthorities());
        return authenticationManager.authenticate(authentication);
    }
}
