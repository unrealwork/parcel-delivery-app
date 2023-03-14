package parcel.delivery.app.common.security.jwt;

public interface JwtProvider {
    JwtToken parse(String token);

    String generate(JwtToken jwtToken);

    boolean validate(String token);
}
