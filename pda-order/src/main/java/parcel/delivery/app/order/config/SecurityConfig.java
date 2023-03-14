package parcel.delivery.app.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import parcel.delivery.app.common.error.ErrorHandler;
import parcel.delivery.app.common.security.config.JwtAuthConfigurer;

import static org.springframework.http.HttpMethod.GET;
import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_ORDERS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public SecurityFilterChain configure(HttpSecurity http, ErrorHandler errorHandler, JwtAuthConfigurer jwtAuthConfigurer) throws Exception {
        // @formatter:off
        return http
                .csrf().disable()
                .apply(jwtAuthConfigurer)
                .and()
                .authorizeHttpRequests()
                    .requestMatchers(GET,"/orders").hasAuthority(VIEW_ORDERS.getAuthority())
                    .anyRequest().denyAll()
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(errorHandler)
                    .accessDeniedHandler(errorHandler)
                .and()
                .build();
        // @formatter:on
    }
}
