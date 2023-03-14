package parcel.delivery.app.common.annotations;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

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
@Inherited
@Documented
public @interface PdaSpringApp {
    String ROOT_PACKAGE = "parcel.delivery.app";
}
