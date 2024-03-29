package parcel.delivery.app.gateway;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;


@Data
@AllArgsConstructor
public class OpenApiService {
    @NotBlank
    private String host;
    private int port;
    private Set<String> endpoints;
}
