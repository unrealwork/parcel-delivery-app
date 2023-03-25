package parcel.delivery.app.gateway;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class MicroserviceProperty {
    @NotBlank
    private String host;
    private int port;
    private String openApiUrl;
}
