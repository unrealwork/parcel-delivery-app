package parcel.delivery.app.common.security.jwt;

import java.time.Duration;

public interface JwtProvider {
    JwtToken parse(String token);

    String generate(JwtToken jwtToken, Duration expirationDuration);

    String generate(JwtToken jwtToken);

    boolean validate(String token);
}
