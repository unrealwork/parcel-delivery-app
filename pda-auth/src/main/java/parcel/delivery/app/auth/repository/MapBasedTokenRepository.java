package parcel.delivery.app.auth.repository;

import org.springframework.stereotype.Service;
import parcel.delivery.app.auth.domain.TokenPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MapBasedTokenRepository implements TokenRepository {
    private final Map<String, TokenPair> tokenPairByAccess = new ConcurrentHashMap<>();
    private final Map<String, TokenPair> tokenPairByRefresh = new ConcurrentHashMap<>();

    @Override
    public void addTokenPair(TokenPair tokenPair) {
        if (!isAccessTokenExist(tokenPair.accessToken())) {
            tokenPairByAccess.putIfAbsent(tokenPair.accessToken(), tokenPair);
            tokenPairByAccess.putIfAbsent(tokenPair.refreshToken(), tokenPair);
        }
    }

    @Override
    public boolean isAccessTokenExist(String accessToken) {
        return tokenPairByAccess.containsKey(accessToken);
    }

    @Override
    public boolean isRefreshTokenExist(String refreshToken) {
        return tokenPairByRefresh.containsKey(refreshToken);
    }

    @Override
    public boolean revokeAccessToken(String accessToken) {
        if (!isAccessTokenExist(accessToken)) {
            TokenPair tokenPair = tokenPairByAccess.remove(accessToken);
            return tokenPairByRefresh.remove(tokenPair.accessToken(), tokenPair);
        }
        return false;
    }
}
