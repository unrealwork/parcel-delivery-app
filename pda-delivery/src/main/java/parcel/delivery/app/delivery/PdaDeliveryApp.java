package parcel.delivery.app.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
@Slf4j
public class PdaDeliveryApp {
    public static void main(String... args) {
        SpringApplication.run(PdaDeliveryApp.class, args);
    }
}
