package parcel.delivery.app.gateway.config;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.gateway.OpenApiService;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith( {SpringExtension.class, MockitoExtension.class})
class OpenApiServiceRegistryTest {
    @Autowired
    @InjectMocks
    private OpenApiServiceRegistry registry;
    @MockBean
    private OpenAPIParser swaggerParser;

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("Test cached registry")
    void restRetrieve() {
        OpenAPI openAPI = new OpenAPI();
        SwaggerParseResult parserResult = new SwaggerParseResult();
        parserResult.setOpenAPI(openAPI);
        Mockito.when(swaggerParser.readLocation(any(), any(), any()))
                .thenReturn(parserResult);
        OpenApiService msProperty = new OpenApiService("localhost", 8085);
        OpenAPI registryResult = registry.retrieve(msProperty);
        assertThat(registryResult, equalTo(openAPI));
        Mockito.when(swaggerParser.readLocation(Mockito.anyString(), any(), any()))
                .thenReturn(new SwaggerParseResult());
        assertThat(registry.retrieve(msProperty), sameInstance(registryResult));
    }
}
