package parcel.delivery.app.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.auth.controller.api.request.SignInRequest;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.controller.api.response.AuthData;
import parcel.delivery.app.auth.controller.api.response.SignInResponse;
import parcel.delivery.app.auth.service.AuthenticationService;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(
        name = "Authentication API",
        description = "Endpoints related to user authentication"
)
public class AuthController {
    private final AuthenticationService authenticationService;


    @Operation(summary = "Register new user",
            description = "Provide data required for the registration process of a user of the app",
            responses = {
                    @ApiResponse(description = "Successful registration", responseCode = "204")
            }
    )
    @PostMapping("/signup")
    @SecurityRequirements
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        authenticationService.signUp(signUpRequest);
        return ResponseEntity.noContent()
                .build();
    }


    @Operation(summary = "Sign in",
            description = "Retrieve access token using user's credentials. Single entrypoint for all types of accounts.")
    @PostMapping("/signin")
    @SecurityRequirements
    public ResponseEntity<SignInResponse> signin(@Valid @RequestBody SignInRequest userSignInRequest) {
        SignInResponse result = authenticationService.signIn(userSignInRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signup/courier")
    @AuthPolicy(RolePrivilege.CREATE_COURIER_USER)
    @Operation(summary = "Create courier account",
            description = "Creates courier account with provided registration data",
            responses = {
                    @ApiResponse(description = "Successful courier account creation", responseCode = "204")
            }
    )
    public ResponseEntity<Void> courierSignup(@RequestBody SignUpRequest courierSignUp) {
        authenticationService.signUp(courierSignUp, UserRole.COURIER);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/me")
    @AuthPolicy
    @Operation(summary = "Retrieve authorization data",
            description = "Describes authorization data containing in provided JWT token")
    public ResponseEntity<AuthData> me() {
        AuthData authData = authenticationService.me();
        return ResponseEntity.ok(authData);
    }
}
