package parcel.delivery.app.common.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.util.WebUtil;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ApiRestClientTest {
    private static final String TEST_URL = "/test";

    @Autowired
    private ApiRestClient apiRestClient;

    @Test
    @DisplayName("Should perform get request in rest format")
    void testGet() throws Exception {
        apiRestClient.get(TEST_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.method").value(HttpMethod.GET.name()));
    }

    @Test
    @DisplayName("Should perform get request in rest format with bearerTokent")
    void testGetAuth() throws Exception {
        apiRestClient.get(TEST_URL, "token")
                .andExpect(jsonPath("$.auth").isString())
                .andExpect(jsonPath("$.auth").value(startsWith(WebUtil.BEARER)))
                .andExpect(jsonPath("$.method").value(HttpMethod.GET.name()));
    }

    @Test
    @DisplayName("Should perform post request with request object")
    void testPost() throws Exception {
        apiRestClient.post(new TestData(1), TEST_URL)
                .andExpect(jsonPath("$.data.val").value(1))
                .andExpect(jsonPath("$.method").value(HttpMethod.POST.name()));
    }


    @Test
    @DisplayName("Should perform post request with raw json")
    void testPostJson() throws Exception {
        String req = """
                {"val":1}
                """.trim();
        apiRestClient.postJson(req, TEST_URL)
                .andExpect(jsonPath("$.data.val").value(1))
                .andExpect(jsonPath("$.method").value(HttpMethod.POST.name()));

    }


    @Test
    @DisplayName("Should perform put request with request object")
    void testPut() throws Exception {
        apiRestClient.put(new TestData(1), TEST_URL)
                .andExpect(jsonPath("$.data.val").value(1))
                .andExpect(jsonPath("$.method").value(HttpMethod.PUT.name()));
    }

    @Test
    @DisplayName("Should perform post request with request object")
    void testPutJson() throws Exception {
        String req = """
                {"val":1}
                """.trim();
        apiRestClient.putJson(req, TEST_URL)
                .andExpect(jsonPath("$.data.val").value(1))
                .andExpect(jsonPath("$.method").value(HttpMethod.PUT.name()));
    }


    @Configuration
    @EnableWebSecurity
    @Import( {ApiRestClient.class, Controller.class, ObjectMapper.class})
    static class Config {
        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
            return http
                    .csrf()
                    .disable()
                    .exceptionHandling()
                    .and()
                    .authorizeHttpRequests()
                    .anyRequest()
                    .permitAll()
                    .and()
                    .build();
        }
    }

    @RestController
    @RequestMapping(TEST_URL)
    @EnableAutoConfiguration
    static class Controller {
        @GetMapping
        ResponseEntity<TestResponse> get(HttpServletRequest request) {
            return ResponseEntity.ok(response(null, request));
        }

        @PostMapping
        ResponseEntity<TestResponse> post(@RequestBody TestData data, HttpServletRequest request) {
            TestResponse resp = response(data, request);
            return ResponseEntity.ok(resp);
        }

        @PutMapping
        ResponseEntity<TestResponse> put(@RequestBody TestData data, HttpServletRequest request) {
            TestResponse resp = response(data, request);
            return ResponseEntity.ok(resp);
        }

        private TestResponse response(TestData data, HttpServletRequest request) {
            String authHeader = request.getHeader(AUTHORIZATION);
            return new TestResponse(data, authHeader, request.getMethod());
        }
    }

    record TestData(int val) {
    }

    record TestResponse(TestData data, String auth, String method) {
    }
}
