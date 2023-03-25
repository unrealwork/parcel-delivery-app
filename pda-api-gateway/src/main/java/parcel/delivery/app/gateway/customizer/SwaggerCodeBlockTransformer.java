package parcel.delivery.app.gateway.customizer;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webflux.ui.SwaggerIndexPageTransformer;
import org.springdoc.webflux.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.resource.ResourceTransformerChain;
import org.springframework.web.reactive.resource.TransformedResource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
public class SwaggerCodeBlockTransformer
        extends SwaggerIndexPageTransformer {
    @Value("classpath:/static/swagger/index.html")
    private Resource swaggerIndexHtml;
    @Value("classpath:/static/swagger/css/theme.css")
    private Resource themeCss;

    /**
     * Instantiates a new Swagger index transformer.
     *
     * @param swaggerUiConfig           the swagger ui config
     * @param swaggerUiOAuthProperties  the swagger ui o auth properties
     * @param swaggerUiConfigParameters the swagger ui config parameters
     * @param swaggerWelcomeCommon      the swagger welcome common
     * @param objectMapperProvider      the object mapper provider
     */
    public SwaggerCodeBlockTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, SwaggerWelcomeCommon swaggerWelcomeCommon, ObjectMapperProvider objectMapperProvider) {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, swaggerWelcomeCommon, objectMapperProvider);
    }

    @Override
    public Mono<Resource> transform(ServerWebExchange serverWebExchange, Resource resource, ResourceTransformerChain resourceTransformerChain) {
        String filename = resource.getFilename();
        if ("index.html".equals(filename)) {
            return replaceWithLocalResource(resource, swaggerIndexHtml);
        }
        return super.transform(serverWebExchange, resource, resourceTransformerChain);
    }

    private Mono<Resource> replaceWithLocalResource(Resource resource, Resource localResource) {
        return Mono.fromSupplier(() -> {
            try {
                return new TransformedResource(resource, localResource.getContentAsByteArray());
            } catch (IOException e) {
                log.warn("Failed to load custom swagger page");
                throw new IllegalStateException(e);
            }
        });
    }
}
