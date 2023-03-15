package parcel.delivery.app.auth;

import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
public class PdaAuthApplication {

    static final String APP_ROOT_PACKAGE = "parcel.delivery.app";

    public static void main(String[] args) {
        SpringApplication.run(PdaAuthApplication.class, args);
    }
}
