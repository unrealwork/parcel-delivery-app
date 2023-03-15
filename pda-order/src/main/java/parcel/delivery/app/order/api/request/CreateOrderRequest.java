package parcel.delivery.app.order.api.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public record CreateOrderRequest(@NotBlank String description,
                                 @NotNull @DecimalMin("0.0") @DecimalMax("100.0") @Digits(integer = 3, fraction = 3)
                                 BigDecimal weight) implements Serializable {
    public CreateOrderRequest(String description, String weight) {
        this(description, new BigDecimal(weight));
    }
}
