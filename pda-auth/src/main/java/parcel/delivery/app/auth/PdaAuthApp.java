package parcel.delivery.app.auth;

import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
public class PdaAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(PdaAuthApp.class, args);
    }
}
