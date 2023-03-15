package parcel.delivery.app.common.security.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.config.properties.JwtProviderProperties;
import parcel.delivery.app.common.helper.SecuredTestingConfig;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.common.security.filters.JwtAuthenticationFilter;
import parcel.delivery.app.common.security.jwt.JwtProvider;
import parcel.delivery.app.common.security.jwt.JwtProviderImpl;
import parcel.delivery.app.common.security.jwt.JwtToken;

import java.util.Set;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.common.util.WebUtil.bearerHeader;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, 
        classes = {JwtAuthConfigurerTest.Config.class, SecuredTestingConfig.class})
class JwtAuthConfigurerTest {
    public static final String TEST_URL = "/test";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("Valid JWT Token should provide access to test resource")
    void testAvailability() throws Exception {
        JwtToken jwtToken = JwtToken
                .builder()
                .userType(UserType.USER)
                .clientId("test@mail.com")
                .privileges(Set.of(RolePrivilege.CREATE_COURIER_USER))
                .build();
        String bearerToken = jwtProvider.generate(jwtToken);
        mvc.perform(get(TEST_URL)
                        .header(AUTHORIZATION, bearerHeader(bearerToken)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Access should be restricted for unauthenticated user")
    void testUnavailability() throws Exception {
        mvc.perform(get(TEST_URL))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @RestController
    static class Controller {
        @GetMapping(TEST_URL)
        public ResponseEntity<?> test() {
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }

    @Configuration
    @Import( {JwtAuthConfigurer.class, JwtProviderImpl.class, JwtAuthenticationFilter.class, Controller.class})
    @EnableConfigurationProperties(value = JwtProviderProperties.class)
    @EnableWebSecurity
    @EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
    static class Config {
        @Bean
        public SecurityFilterChain configure(HttpSecurity http, JwtAuthConfigurer jwtAuthConfigurer) throws Exception {
            return http
                    .csrf()
                    .disable()
                    .apply(jwtAuthConfigurer)
                    .and()
                    .authorizeHttpRequests()
                    .requestMatchers(TEST_URL)
                    .authenticated()
                    .anyRequest()
                    .denyAll()
                    .and()
                    .build();
        }
    }
}
