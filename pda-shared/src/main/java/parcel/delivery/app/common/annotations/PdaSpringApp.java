package parcel.delivery.app.common.annotations;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication(scanBasePackages = {PdaSpringApp.ROOT_PACKAGE})
@ConfigurationPropertiesScan(basePackages = {PdaSpringApp.ROOT_PACKAGE})
@SecurityScheme(
        name = "jwt",
        description = "Authentication and authorization in the app are organized using JWT tokens.",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Inherited
@Documented
@EnableAsync
public @interface PdaSpringApp {
    String ROOT_PACKAGE = "parcel.delivery.app";
}
