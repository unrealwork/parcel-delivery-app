package parcel.delivery.app.gateway.config;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilderFactory;
import parcel.delivery.app.gateway.OpenApiService;
import parcel.delivery.app.gateway.config.properties.AppProxyProperties;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
class OpenApiServiceRegistry {
    private final AppProxyProperties properties;
    private final Map<OpenApiService, OpenAPI> microserviceApiFetcherMap = new ConcurrentHashMap<>();
    private final UriBuilderFactory uriBuilderFactory;
    private final OpenAPIParser parser;


    public OpenAPI retrieve(OpenApiService openApiService) {
        return microserviceApiFetcherMap.computeIfAbsent(openApiService, this::fetchOpenApiDoc);
    }

    private OpenAPI fetchOpenApiDoc(OpenApiService openApiService) {
        String openApiUrl = properties.getOpenApiUrl();
        URI uri = uriBuilderFactory.builder()
                .scheme("http")
                .host(openApiService.getHost())
                .port(openApiService.getPort())
                .path(openApiUrl)
                .build();
        SwaggerParseResult swaggerParseResult = parser.readLocation(uri.toString(), null, null);
        if (swaggerParseResult == null) {
            throw new IllegalStateException("Failed to parse Open API specification from " + uri);
        }
        return swaggerParseResult
                .getOpenAPI();
    }
}
