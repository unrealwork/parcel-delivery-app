package parcel.delivery.app.delivery.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.test.client.ApiRestClient;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SecurityConfigTest {
    @Autowired
    private ApiRestClient client;

    @ParameterizedTest
    @CsvSource(textBlock = """
            GET,/actuator/health,200
            GET,/nonexist/,404
            """)
    @DisplayName("Should return correct statuses in unauthenticated mode")
    void testUnauthenticatedAccess(String method, String url, int status) throws Exception {
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        client.request(httpMethod, null, url)
                .andExpect(status().is(status));
    }
}
