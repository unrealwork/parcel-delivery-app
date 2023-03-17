package parcel.delivery.app.auth.service;

import parcel.delivery.app.auth.controller.api.request.SignInRequest;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.controller.api.response.AuthData;
import parcel.delivery.app.auth.controller.api.response.SignInResponse;
import parcel.delivery.app.common.security.core.UserType;

public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequestDto, UserType userType);

    default void signUp(SignUpRequest signUpRequest) {
        signUp(signUpRequest, UserType.USER);
    }

    SignInResponse signIn(SignInRequest signInRequest);

    AuthData me();
}
