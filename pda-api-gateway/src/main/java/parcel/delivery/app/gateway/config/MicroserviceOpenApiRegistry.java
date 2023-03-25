package parcel.delivery.app.gateway.config;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilderFactory;
import parcel.delivery.app.gateway.MicroserviceProperty;
import parcel.delivery.app.gateway.config.properties.AppProxyProperties;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
class MicroserviceOpenApiRegistry {
    private final AppProxyProperties properties;
    private final Map<MicroserviceProperty, OpenAPI> microserviceApiFetcherMap = new ConcurrentHashMap<>();
    private final UriBuilderFactory uriBuilderFactory;
    private final OpenAPIParser parser = new OpenAPIParser();


    public OpenAPI retrieve(MicroserviceProperty microserviceProperty) {
        return microserviceApiFetcherMap.computeIfAbsent(microserviceProperty, this::fetchOpenApiDoc);
    }

    private OpenAPI fetchOpenApiDoc(MicroserviceProperty microserviceProperty) {
        String openApiUrl = microserviceProperty.getOpenApiUrl();
        if (openApiUrl == null) {
            openApiUrl = properties.getOpenApiUrl();
        }
        URI uri = uriBuilderFactory.builder()
                .scheme("http")
                .host(microserviceProperty.getHost())
                .port(microserviceProperty.getPort())
                .path(openApiUrl)
                .build();
        return parser.readLocation(uri.toString(), null, null)
                .getOpenAPI();
    }
}
