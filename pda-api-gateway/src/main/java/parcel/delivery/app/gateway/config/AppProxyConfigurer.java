package parcel.delivery.app.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilderFactory;
import parcel.delivery.app.gateway.OpenApiService;
import parcel.delivery.app.gateway.config.properties.AppProxyProperties;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppProxyConfigurer implements RouteLocatorConfigurer {
    private final AppProxyProperties properties;
    private final OpenApiServiceRegistry openApiRegistry;

    private final UriBuilderFactory uriBuilderFactory;

    @Override
    public RouteLocator configure(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder builder = routeLocatorBuilder.routes();
        if (properties != null) {
            properties.getMicroservices()
                    .forEach((serviceId, property) ->
                            addMicroserviceRoutes(builder, serviceId, property));
        }
        return builder.build();
    }

    private void addMicroserviceRoutes(RouteLocatorBuilder.Builder builder, String serviceId, OpenApiService property) {
        try {
            final OpenAPI openAPI = openApiRegistry.retrieve(property);
            if (openAPI != null) {
                for (String apiEndpoint : property.getEndpoints()) {
                    builder = builder.route(predicateSpec -> builtApiPathRoute(predicateSpec, property, apiEndpoint));
                }
                builder.route(serviceId + "-openapi",
                        ps -> buildOpenApiRoute(serviceId, property, ps));
            } else {
                log.warn("Failed to retrieve");
            }
        } catch (Exception e) {
            log.warn("Failed to setup routes for microservice {}", property, e);
        }
    }

    private Buildable<Route> buildOpenApiRoute(String serviceId, OpenApiService property, PredicateSpec ps) {
        String proxiedOpenApiPath = uriBuilderFactory.uriString(properties.getOpenApiUrl() + "/{serviceId}")
                .build(serviceId)
                .toString();

        return ps.path(false, proxiedOpenApiPath)
                .filters(gfs -> gfs.rewritePath(proxiedOpenApiPath, properties.getOpenApiUrl()))
                .uri(uriForMicroservice(property));
    }


    private Buildable<Route> builtApiPathRoute(PredicateSpec predicateSpec, OpenApiService property, String apiEndpoint) {
        String predicate = properties.getPrefix() + apiEndpoint + "/**";
        String filterPattern = properties.getPrefix() + "(?<remaining>.*)";
        String mappingPattern = "$\\{remaining}";
        return predicateSpec.path(false, predicate)
                .filters(gfs -> gfs.rewritePath(filterPattern, mappingPattern))
                .uri(uriForMicroservice(property));
    }

    private String uriForMicroservice(OpenApiService property) {
        return uriBuilderFactory.builder()
                .scheme("http")
                .host(property.getHost())
                .port(property.getPort())
                .build()
                .toString();
    }
}
