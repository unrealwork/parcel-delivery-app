package parcel.delivery.app.gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import parcel.delivery.app.gateway.config.RouteLocatorConfigurer;

@SpringBootApplication
@ConfigurationPropertiesScan("parcel.delivery.app.gateway.config.properties")
@OpenAPIDefinition(info = @Info(
        title = "API for Parcel Delivery App",
        description = "Contains methods related to authentication, orders, delivery",
        version = "0.0.1"),
        security = @SecurityRequirement(name = "jwt"))
@SecurityScheme(
        name = "jwt",
        description = "Authentication and authorization in the app are organized using JWT tokens.",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Slf4j
public class PdaGatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(PdaGatewayApp.class);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, RouteLocatorConfigurer configurer) {
        return configurer.configure(builder);
    }


    @Bean
    public UriBuilderFactory uriBuilderFactory() {
        return new DefaultUriBuilderFactory();
    }
}
