package parcel.delivery.app.auth.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import parcel.delivery.app.auth.security.jwt.JwtProvider;
import parcel.delivery.app.auth.security.jwt.JwtToken;
import parcel.delivery.app.auth.security.jwt.util.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isValidAuthBearerHeader(authHeader)) {
            final String bearerToken = authHeader.substring(BEARER.length());
            if (StringUtils.hasText(bearerToken) && jwtProvider.validate(bearerToken)) {
                final JwtToken jwtToken = jwtProvider.parse(bearerToken);
                final Authentication authentication = JwtUtil.authenticationFromToken(jwtToken);
                final SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthBearerHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER);
    }
}
