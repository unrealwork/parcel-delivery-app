package parcel.delivery.app.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PdaAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdaAuthApplication.class, args);
    }
}
