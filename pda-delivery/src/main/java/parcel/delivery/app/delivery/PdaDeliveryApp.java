package parcel.delivery.app.delivery;

import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
public class PdaDeliveryApp {
    public static void main(String... args) {
        SpringApplication.run(PdaDeliveryApp.class, args);
    }
}
