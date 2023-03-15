package parcel.delivery.app.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import parcel.delivery.app.common.annotations.PdaSpringApp;
import parcel.delivery.app.common.error.ErrorHandler;
import parcel.delivery.app.common.security.config.JwtAuthConfigurer;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static parcel.delivery.app.common.security.core.RolePrivilege.CREATE_COURIER_USER;

/**
 * Security configuration
 *
 * @author unrealwork
 */
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
                    .requestMatchers(POST,"/auth/signup").permitAll()
                    .requestMatchers(POST,"/auth/signin").permitAll()
                    .requestMatchers(POST,"/auth/signup/courier")
                        .hasAuthority(CREATE_COURIER_USER.getAuthority())    
                    .requestMatchers(GET,"/auth/me").authenticated()
                    .requestMatchers("/**").denyAll()
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(errorHandler)
                    .accessDeniedHandler(errorHandler)
                .and()
                .build();
                // @formatter:on
    }

    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder encoder, UserDetailsService uds) {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(uds);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
