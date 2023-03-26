package parcel.delivery.app.gateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import parcel.delivery.app.gateway.OpenApiService;
import parcel.delivery.app.gateway.config.properties.AppProxyProperties;

import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class OpenApiAggregationCustomizer implements OpenApiCustomizer {
    private final AppProxyProperties properties;
    private final OpenApiServiceRegistry registry;

    @Override
    public void customise(OpenAPI rootApi) {
        Map<String, OpenApiService> microservices = properties.getMicroservices();
        Paths paths = rootApi.getPaths();
        Components components = rootApi.getComponents();
        microservices.forEach((s, property) -> {
            Optional<OpenAPI> msApi = Optional.ofNullable(registry.retrieve(property));
            addPaths(paths, msApi);
            addSchemas(components, msApi);
            addTags(rootApi, msApi);
        });

    }

    private void addTags(OpenAPI rootApi, Optional<OpenAPI> msApi) {
        msApi.map(OpenAPI::getTags)
                .ifPresent(tags -> {
                    for (Tag tag : tags) {
                        rootApi.addTagsItem(tag);
                    }
                });
    }

    private void addPaths(@NonNull Paths paths, Optional<OpenAPI> api) {
        api.map(OpenAPI::getPaths)
                .ifPresent(apiPaths -> apiPaths.forEach((path, pathItem) -> {
                    final String proxiedPath = properties.getPrefix() + path;
                    paths.addPathItem(proxiedPath, pathItem);
                }));
    }

    private void addSchemas(@NonNull Components components, Optional<OpenAPI> api) {
        api.map(OpenAPI::getComponents)
                .map(Components::getSchemas)
                .ifPresent(apiSchemas -> apiSchemas.forEach(components::addSchemas));
    }
}
