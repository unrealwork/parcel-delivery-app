package parcel.delivery.app.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.auth.api.models.request.SignInRequest;
import parcel.delivery.app.auth.api.models.request.SignUpRequest;
import parcel.delivery.app.auth.api.models.response.AuthData;
import parcel.delivery.app.auth.api.models.response.SignInResponse;
import parcel.delivery.app.auth.service.AuthenticationService;
import parcel.delivery.app.common.security.core.UserType;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @GetMapping("/me")
    public ResponseEntity<AuthData> me() {
        AuthData authData = authenticationService.me();
        return ResponseEntity.ok(authData);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        authenticationService.signUp(signUpRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signin(@Valid @RequestBody SignInRequest userSignInRequest) {
        SignInResponse result = authenticationService.signIn(userSignInRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signup/courier")
    public ResponseEntity<Void> courierSignup(@RequestBody SignUpRequest courierSignUp) {
        authenticationService.signUp(courierSignUp, UserType.COURIER);
        return ResponseEntity.noContent()
                .build();
    }
}
