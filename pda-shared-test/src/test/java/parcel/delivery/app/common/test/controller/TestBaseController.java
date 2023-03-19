package parcel.delivery.app.common.test.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.test.client.ApiRestClient;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestBaseController extends BaseControllerTest {
    private static final String TEST = "/test";

    @Test
    @WithMockUser
    @DisplayName("Should setup via BaseControllerTest")
    void test() throws Exception {
        client.get(TEST)
                .andExpect(status().isOk());
    }

    @Configuration
    @EnableWebSecurity
    @EnableAutoConfiguration
    @Import( {Controller.class, ApiRestClient.class})
    static class Config {
    }

    @RestController
    static class Controller {
        @GetMapping(TEST)
        public void test() {

        }
    }
}
