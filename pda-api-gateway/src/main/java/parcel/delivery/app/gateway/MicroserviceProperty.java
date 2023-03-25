package parcel.delivery.app.gateway;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MicroserviceProperty {
    @NotBlank
    private String host;
    private int port;
}
