package parcel.delivery.app.delivery.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.messaging.events.SignedUpEvent;
import parcel.delivery.app.common.security.core.UserRole;

@RestController
@RequiredArgsConstructor
public class ProduceController {
    private final StreamBridge streamBridge;
    private final ApplicationContext applicationContext;

    @GetMapping("/test")
    public void produce() {
        streamBridge.send("produce", new SignedUpEvent(UserRole.USER, "unrealwork@gmail.com"));
    }
}
