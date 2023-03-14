package parcel.delivery.app.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {PdaAuthApplication.APP_ROOT_PACKAGE})
@ConfigurationPropertiesScan(basePackages = {PdaAuthApplication.APP_ROOT_PACKAGE})
public class PdaAuthApplication {

    static final String APP_ROOT_PACKAGE = "parcel.delivery.app";

    public static void main(String[] args) {
        SpringApplication.run(PdaAuthApplication.class, args);
    }
}
