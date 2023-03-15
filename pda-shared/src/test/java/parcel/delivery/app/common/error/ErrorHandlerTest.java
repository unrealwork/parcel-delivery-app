package parcel.delivery.app.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.helper.SecuredTestingConfig;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {ErrorHandlerTest.Config.class, SecuredTestingConfig.class, ObjectMapper.class})
class ErrorHandlerTest {
    private static final String TEST_URL = "/test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @CsvSource( {
            "/test,401",
            "/nonexist,401"
    })
    @DisplayName("Should required authentication for anonymous users")
    void testExceptionHandlingForAnonymous(String url, int expectedStatus) throws Exception {
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.code").isNumber());

    }

    @ParameterizedTest
    @CsvSource( {
            "/test,1,400",
            "/test,13,500",
            "/nonexist,5,403"
    })
    @WithMockUser
    @DisplayName("Should correctly handle  app logic exceptions ")
    void testAppLogicRelatedExceptionHandling(String url, int reqValue, int expectedStatus) throws Exception {
        TestData testData = new TestData(reqValue);
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testData)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Configuration
    @Import( {ErrorHandler.class, ExecptionHandler.class, TestController.class})
    @EnableWebSecurity
    @EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
    static class Config {
        @Bean
        public SecurityFilterChain configure(HttpSecurity http, ErrorHandler execptionHandler) throws Exception {
            return http
                    .csrf()
                    .disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(execptionHandler)
                    .accessDeniedHandler(execptionHandler)
                    .and()
                    .authorizeHttpRequests()
                    .requestMatchers(POST, TEST_URL)
                    .authenticated()
                    .anyRequest()
                    .denyAll()
                    .and()
                    .build();
        }
    }

    @RestController
    static class TestController {
        @PostMapping(TEST_URL)
        public ResponseEntity<?> test(@Valid @RequestBody TestData req) {
            if (req.value() == 13) {
                throw new IllegalArgumentException("Unlucky number");
            }
            return ResponseEntity.noContent()
                    .build();
        }
    }

    record TestData(@Min(5) int value) {
    }
}
