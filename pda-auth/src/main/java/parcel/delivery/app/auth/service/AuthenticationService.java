package parcel.delivery.app.auth.service;

import parcel.delivery.app.auth.api.models.request.SignInRequest;
import parcel.delivery.app.auth.api.models.request.SignUpRequest;
import parcel.delivery.app.auth.api.models.response.AuthData;
import parcel.delivery.app.auth.api.models.response.SignInResponse;
import parcel.delivery.app.auth.security.core.UserType;

public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequestDto, UserType userType);

    default void signUp(SignUpRequest signUpRequest) {
        signUp(signUpRequest, UserType.USER);
    }

    SignInResponse signIn(SignInRequest signInRequest);

    AuthData me();
}
