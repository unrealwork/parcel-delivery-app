package parcel.delivery.app.common.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.io.Deserializer;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import parcel.delivery.app.common.config.properties.JwtProviderProperties;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.util.DateTimeUtil;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public final class JwtProviderImpl implements JwtProvider {
    private static final String SUPER_USER_TOKEN = "special_key";

    private static final JwtToken SUPER_USER_AUTH_DATA = JwtToken.builder()
            .userRole(UserRole.SUPERUSER)
            .clientId("root@user.su")
            .expiresAt(Instant.MAX)
            .privileges(Set.copyOf(UserRole.SUPERUSER.privileges()))
            .issuedAt(Instant.EPOCH)
            .build();
    private static final Map<String, Class<?>> CLASS_MAP = Maps.<String, Class<?>>of(JwtClaimKeys.AUTHORIZTATION_DATA, JwtAuthData.class)
            .build();
    @SuppressWarnings({"squid:S3740", "unchecked"})
    private static final Deserializer<Map<String, ?>> JACKSON_DESERIALIZER = new JacksonDeserializer(CLASS_MAP);
    private final Key secretKey;
    private final Duration expirationDuration;
    private final JwtProviderProperties conf;

    public JwtProviderImpl(JwtProviderProperties conf) {
        this.secretKey = conf.isEnabled() ? getSignatureKey(conf.getSecretKey()) : null;
        this.conf = conf;
        this.expirationDuration = conf.getExpirationDuration();
        if (conf.isEnabled()) {
            validateSecretKey(conf.getSecretKey());
        }
    }


    public void validateSecretKey(String secretKey) {
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalStateException("JWT secret key should be specified");
        }
    }

    @Override
    public JwtToken parse(String token) {
        if (conf.isSpecialKeyAllowed() && SUPER_USER_TOKEN.equals(token)) {
            return SUPER_USER_AUTH_DATA;
        }
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
                .userRole(authData.userRole())
                .privileges(authData.privileges())
                .build();
    }

    private Instant dateToInstant(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime());
    }

    @Override
    public String generate(JwtToken jwtToken, Duration expirationDuration) {
        final Date issueDate = extractIssueDateFromToken(jwtToken);
        final Date expireDate = calcExpirationDateForToken(jwtToken, issueDate, expirationDuration);

        Map<String, ?> extraClaims = Map.of(JwtClaimKeys.AUTHORIZTATION_DATA, new JwtAuthData(jwtToken.userRole(), jwtToken.privileges()));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(jwtToken.clientId())
                .setIssuedAt(issueDate)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generate(JwtToken jwtToken) {
        return generate(jwtToken, this.expirationDuration);
    }

    private Date calcExpirationDateForToken(JwtToken jwtToken, Date issueDate, Duration expirationDuration) {
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
        } catch (MalformedJwtException | DecodingException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("JWT claims parsing failed: {}", e.getMessage());
        }
        return false;
    }

    private Key getSignatureKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
