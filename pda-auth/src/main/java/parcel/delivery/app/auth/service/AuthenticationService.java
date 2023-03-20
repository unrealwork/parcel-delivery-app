package parcel.delivery.app.auth.service;

import parcel.delivery.app.auth.controller.api.request.SignInRequest;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.controller.api.response.AuthData;
import parcel.delivery.app.auth.controller.api.response.SignInResponse;
import parcel.delivery.app.common.security.core.UserRole;

public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequestDto, UserRole userRole);

    default void signUp(SignUpRequest signUpRequest) {
        signUp(signUpRequest, UserRole.USER);
    }

    SignInResponse signIn(SignInRequest signInRequest);

    AuthData me();
}
