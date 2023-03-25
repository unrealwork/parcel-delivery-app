package parcel.delivery.app.gateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.stereotype.Component;
import parcel.delivery.app.gateway.MicroserviceProperty;
import parcel.delivery.app.gateway.config.properties.AppProxyProperties;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Component
public class OpenApiAggregationCustomizer implements OpenApiCustomizer {
    private final AppProxyProperties properties;
    private final MicroserviceOpenApiRegistry registry;

    @Override
    public void customise(OpenAPI rootApi) {
        Map<String, MicroserviceProperty> microservices = properties.getMicroservices();
        Paths paths = rootApi.getPaths();
        Components components = rootApi.getComponents();
        microservices.forEach((s, property) -> {
            OpenAPI msApi = registry.retrieve(property);
            addPaths(paths, msApi);
            addSchemas(components, msApi);
            addTags(rootApi, msApi.getTags());
        });

    }

    private void addTags(OpenAPI rootApi, List<Tag> tags) {
        for (Tag tag : tags) {
            rootApi.addTagsItem(tag);
        }
    }

    private void addPaths(Paths paths, OpenAPI api) {
        api.getPaths()
                .forEach((path, pathItem) -> {
                    final String proxiedPath = properties.getPrefix() + path;
                    paths.addPathItem(proxiedPath, pathItem);
                });
    }

    private void addSchemas(Components components, OpenAPI api) {
        api.getComponents()
                .getSchemas()
                .forEach(components::addSchemas);
    }
}
