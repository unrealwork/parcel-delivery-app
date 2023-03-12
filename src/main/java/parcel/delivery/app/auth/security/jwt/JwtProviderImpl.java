package parcel.delivery.app.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import parcel.delivery.app.auth.config.properties.JwtProviderProperties;
import parcel.delivery.app.auth.util.DateTimeUtil;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
final class JwtProviderImpl implements JwtProvider {
    private static final Map<String, Class<?>> CLASS_MAP = Maps.<String, Class<?>>of(JwtClaimKeys.AUTHORIZTATION_DATA, JwtAuthData.class)
            .build();
    @SuppressWarnings("squid:S3740")
    private static final JacksonDeserializer<Map<String, ?>> JACKSON_DESERIALIZER = new JacksonDeserializer(CLASS_MAP);
    private final Key secretKey;
    private final Duration expirationDuration;

    JwtProviderImpl(JwtProviderProperties conf) {
        validateSecretKey(conf.secretKey());
        this.secretKey = getSignatureKey(conf.secretKey());
        this.expirationDuration = conf.expirationDuration();

    }


    public void validateSecretKey(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalStateException("JWT secret key should be specified");
        }
    }

    @Override
    public JwtToken parse(String token) {
        Claims allClaims = Jwts.parserBuilder()
                .deserializeJsonWith(JACKSON_DESERIALIZER)
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        final JwtAuthData authData = allClaims.get(JwtClaimKeys.AUTHORIZTATION_DATA, JwtAuthData.class);
        return JwtToken.builder()
                .clientId(allClaims.getSubject())
                .issuedAt(dateToInstant(allClaims.getIssuedAt()))
                .expiresAt(dateToInstant(allClaims.getExpiration()))
                .userType(authData.userType())
                .authorities(authData.privileges())
                .build();
    }

    private Instant dateToInstant(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime());
    }

    @Override
    public String generate(JwtToken jwtToken) {
        final Date issueDate = extractIssueDateFromToken(jwtToken);
        final Date expireDate = calcExpirationDateForToken(jwtToken, issueDate);

        Map<String, ?> extraClaims = Map.of(JwtClaimKeys.AUTHORIZTATION_DATA, new JwtAuthData(jwtToken.userType(), jwtToken.authorities()));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(jwtToken.clientId())
                .setIssuedAt(issueDate)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date calcExpirationDateForToken(JwtToken jwtToken, Date issueDate) {
        return Optional.of(jwtToken)
                .map(JwtToken::expiresAt)
                .map(DateTimeUtil::instantToDate)
                .orElseGet(() -> DateTimeUtil.addDurationToDate(issueDate, expirationDuration));
    }

    private Date extractIssueDateFromToken(JwtToken jwtToken) {
        return Optional.of(jwtToken)
                .map(JwtToken::issuedAt)
                .map(DateTimeUtil::instantToDate)
                .orElseGet(Date::new);
    }


    @Override
    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (ExpiredJwtException exception) {
            return false;
        }
    }

    private Key getSignatureKey(String base64Secret) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
