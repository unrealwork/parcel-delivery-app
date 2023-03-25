package parcel.delivery.app.order;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
@OpenAPIDefinition(info = @Info(
        title = "Order API for Parcel Delivery App",
        description = "Provide access to methods related to users' orders",
        version = "0.0.1"),

        security = @SecurityRequirement(name = "jwt"))
public class PdaOrderApp {
    public static void main(String... args) {
        SpringApplication.run(PdaOrderApp.class, args);
    }
}
