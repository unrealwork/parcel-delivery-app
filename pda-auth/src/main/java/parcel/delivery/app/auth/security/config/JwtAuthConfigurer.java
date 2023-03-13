package parcel.delivery.app.auth.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import parcel.delivery.app.auth.security.filters.JwtAuthenticationFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtAuthenticationFilter authenticationFilter;

    @Override
    public void configure(HttpSecurity security) {
        security.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
