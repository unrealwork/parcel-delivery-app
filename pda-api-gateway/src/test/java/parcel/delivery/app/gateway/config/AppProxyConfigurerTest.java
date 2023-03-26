package parcel.delivery.app.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import parcel.delivery.app.gateway.OpenApiService;
import parcel.delivery.app.gateway.config.properties.AppProxyProperties;

import java.util.Map;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class AppProxyConfigurerTest {

    public static final DockerImageName MOCKSERVER_IMAGE = DockerImageName
            .parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient.class.getPackage()
                    .getImplementationVersion());
    private static final String HELLO_JSON = """
            {"greetings": "hello"}
            """;

    @Container
    public static MockServerContainer mockServer = new MockServerContainer(MOCKSERVER_IMAGE);


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AppProxyProperties properties;


    @Test
    @DisplayName("Should setup proxied methods for methods retrieved from OpenApi specs from other server")
    void configure() {
        try (MockServerClient mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        ) {

            mockServerClient
                    .when(request().withPath("/hello"))
                    .respond(response().withBody(new JsonBody(HELLO_JSON)));

            webTestClient.get()
                    .uri("/api/hello")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody()
                    .json(HELLO_JSON);
        }
    }

    @Test
    @DisplayName("Should create aggregated open API url")
    void testOpenApiUrl() {
        webTestClient.get()
                .uri(properties.getOpenApiUrl())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @TestConfiguration
    static class EarlyConfiguration {
        @MockBean
        private OpenApiServiceRegistry registry;

        @SpyBean
        private AppProxyProperties appProxyProperties;

        @PostConstruct
        public void initMock() {
            if (mockServer.isCreated()) {
                OpenApiService apiService = new OpenApiService(mockServer.getHost(), mockServer.getServerPort());
                Map<String, OpenApiService> microservice = Map.of("test", apiService);
                Mockito.when(appProxyProperties.getMicroservices())
                        .thenReturn(microservice);

                OpenAPI openAPI = new OpenAPI();
                openAPI.setPaths(new Paths());
                openAPI.getPaths()
                        .addPathItem("/hello", new PathItem());
                Mockito.when(registry.retrieve(Mockito.any()))
                        .thenReturn(openAPI);
            }
        }
    }
}
