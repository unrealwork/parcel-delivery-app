package parcel.delivery.app.gateway.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import parcel.delivery.app.gateway.OpenApiService;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "application.proxy")
@Data
@Validated
public class AppProxyProperties {
    private Map<String, OpenApiService> microservices = new HashMap<>();
    @NotNull
    private String openApiUrl;
    @NotNull
    private String prefix;
}
