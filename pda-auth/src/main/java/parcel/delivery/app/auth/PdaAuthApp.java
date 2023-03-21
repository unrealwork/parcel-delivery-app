package parcel.delivery.app.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
@Slf4j
public class PdaAuthApp {

    public static void main(String... args) {
        SpringApplication.run(PdaAuthApp.class, args);
    }
}
