package parcel.delivery.app.auth.repository;

import parcel.delivery.app.auth.domain.TokenPair;

public interface TokenRepository {

    void addTokenPair(TokenPair tokenPair);

    boolean isAccessTokenExist(String accessToken);

    boolean isRefreshTokenExist(String refreshToken);

    boolean revokeAccessToken(String accessToken);
}
