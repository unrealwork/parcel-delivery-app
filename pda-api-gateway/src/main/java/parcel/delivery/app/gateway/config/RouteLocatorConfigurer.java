package parcel.delivery.app.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

public interface RouteLocatorConfigurer {
    RouteLocator configure(RouteLocatorBuilder locatorBuilder);
}
