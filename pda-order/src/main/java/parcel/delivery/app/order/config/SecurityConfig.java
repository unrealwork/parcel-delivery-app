package parcel.delivery.app.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import parcel.delivery.app.common.annotations.PdaSpringApp;
import parcel.delivery.app.common.error.ErrorHandler;
import parcel.delivery.app.common.security.config.JwtAuthConfigurer;

import static org.springframework.http.HttpMethod.PUT;
import static parcel.delivery.app.common.security.core.RolePrivilege.CANCEL_ORDER;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_DESTINATION;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_ORDER_STATUS;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = PdaSpringApp.ROOT_PACKAGE)
public class SecurityConfig {
    @Bean
    @SuppressWarnings("squid:S4502")
    public SecurityFilterChain configure(HttpSecurity http, ErrorHandler errorHandler, JwtAuthConfigurer jwtAuthConfigurer) throws Exception {
        // @formatter:off
        return http
                .csrf().disable()
                .apply(jwtAuthConfigurer)
                .and()
                .authorizeHttpRequests()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers(PUT, "/orders/{id}/status")
                        .hasAuthority(CHANGE_ORDER_STATUS.getAuthority())
                    .requestMatchers(PUT, "/orders/{id}/cancel").hasAuthority(CANCEL_ORDER.getAuthority())
                .requestMatchers(PUT, "/orders/{id}/destination").hasAuthority(CHANGE_DESTINATION.getAuthority())
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(errorHandler)
                    .accessDeniedHandler(errorHandler)
                .and()
                .build();
        // @formatter:on
    }
}
