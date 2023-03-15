package parcel.delivery.app.order;

import org.springframework.boot.SpringApplication;
import parcel.delivery.app.common.annotations.PdaSpringApp;

@PdaSpringApp
public class PdaOrderApp {
    public static void main(String[] args) {
        SpringApplication.run(PdaOrderApp.class, args);
    }
}
