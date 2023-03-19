package parcel.delivery.app.common.security.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.common.helper.SecuredTestingConfig;
import parcel.delivery.app.common.security.AuthenticationFacade;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static parcel.delivery.app.common.security.core.RolePrivilege.BASIC;
import static parcel.delivery.app.common.security.core.RolePrivilege.CANCEL_ORDER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SecuredTestingConfig.class})
@Import(AuthPolicyInterceptorTest.Config.class)
@ExtendWith( {SpringExtension.class})
class AuthPolicyInterceptorTest {
    private static final String TEST_URL_2 = "/test2";
    private static final String TEST_URL = "/test";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthPolicyInterceptor interceptor;

    @MockBean
    private AuthenticationFacade facade;

    @Test
    @WithMockUser
    @DisplayName("Should deny access to resource without valid privilege")
    void testAuthPolicyDenied() throws Exception {
        Mockito.when(facade.privileges())
                .thenReturn(Set.of(BASIC));
        mockMvc.perform(get(TEST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("Should provide access to resource with valid privilege")
    void testAuthPolicyOk() throws Exception {
        Mockito.when(facade.privileges())
                .thenReturn(Set.of(CANCEL_ORDER));
        mockMvc.perform(get(TEST_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should pass hanlder without AuthPolicy")
    void testPassingNoPolicy() throws Exception {
        Mockito.when(facade.privileges())
                .thenReturn(Set.of(CANCEL_ORDER));
        mockMvc.perform(get(TEST_URL_2))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test unusual Handler class")
    void testUnusualHandler() {
        HttpServletRequest reqMock = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse respMock = Mockito.mock(HttpServletResponse.class);
        Assertions.assertDoesNotThrow(() -> interceptor.preHandle(reqMock, respMock, Mockito.mock(Object.class)));
    }

    @Configuration
    @EnableWebSecurity
    @EnableAutoConfiguration
    @Import( {AuthPolicyInterceptor.class, Controller.class})
    @RequiredArgsConstructor
    static class Config implements WebMvcConfigurer {
        private final AuthPolicyInterceptor authPolicyInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(authPolicyInterceptor);
        }

    }

    @RestController
    static class Controller {
        @GetMapping(TEST_URL)
        @AuthPolicy(CANCEL_ORDER)
        void test() {
        }


        @GetMapping(TEST_URL_2)
        void test2() {
        }
    }
}
