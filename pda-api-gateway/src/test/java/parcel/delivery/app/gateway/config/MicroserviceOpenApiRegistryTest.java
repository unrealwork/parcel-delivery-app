package parcel.delivery.app.gateway.config;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.gateway.MicroserviceProperty;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ExtendWith( {SpringExtension.class, MockitoExtension.class})
class MicroserviceOpenApiRegistryTest {
    @Autowired
    private MicroserviceOpenApiRegistry registry;
    @SpyBean
    private OpenAPIParser swaggerParser;

    @Spy
    private SwaggerParseResult parserResult = new SwaggerParseResult();
    @Mock
    private OpenAPI openAPI = new OpenAPI();

    @BeforeEach
    void setup() {
        parserResult.setOpenAPI(openAPI);
        Mockito.when(swaggerParser.readLocation("http://localhost:8085/v3/api-docs", null, null))
                .thenReturn(parserResult);

    }

    @Test
    @DisplayName("Test cached registry")
    void restRetrieve() {
        MicroserviceProperty msProperty = new MicroserviceProperty("localhost", 8085);
        OpenAPI registryResult = registry.retrieve(msProperty);
        assertThat(registryResult, equalTo(openAPI));
        Mockito.when(swaggerParser.readLocation(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(new SwaggerParseResult());
        assertThat(registry.retrieve(msProperty), sameInstance(registryResult));
    }
}
