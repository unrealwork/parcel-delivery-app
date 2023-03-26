package parcel.delivery.app.gateway.config.properties;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import parcel.delivery.app.gateway.OpenApiService;
import parcel.delivery.app.gateway.config.RouteLocatorConfigurer;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@TestPropertySource(properties = {
        "application.proxy.open-api-url=/v3/api-docs",
        "application.proxy.microservices.auth.host=localhost",
        "application.proxy.microservices.auth.port=8081"
})
class AppProxyPropertiesTest {
    @Autowired
    private AppProxyProperties properties;
    @MockBean
    private RouteLocatorConfigurer routeLocator;

    @Test
    @DisplayName("Should inject properties correctly")
    void test() {
        Mockito.when(routeLocator.configure(Mockito.any()))
                .thenReturn(null);
        assertThat(properties, notNullValue());
        assertThat(properties.getOpenApiUrl(), CoreMatchers.equalTo("/v3/api-docs"));
        Map<String, OpenApiService> microservices = properties.getMicroservices();
        assertThat(microservices, notNullValue());
        assertThat(microservices.size(), equalTo(1));
        assertThat(microservices, hasKey("auth"));
        OpenApiService property = microservices
                .get("auth");
        assertThat(property.getHost(), equalTo("localhost"));
        assertThat(property.getPort(), equalTo(8081));
    }
}
