package parcel.delivery.app.delivery;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
@Slf4j
@OpenAPIDefinition(info = @Info(
        title = "Delivery API for Parcel Delivery App",
        description = "Provide access to methods related to delivery of orders",
        version = "0.0.1"),
        security = @SecurityRequirement(name = "jwt"))
public class PdaDeliveryApp {
    public static void main(String... args) {
        SpringApplication.run(PdaDeliveryApp.class, args);
    }
}
