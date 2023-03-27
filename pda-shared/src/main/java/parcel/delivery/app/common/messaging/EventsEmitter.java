package parcel.delivery.app.common.messaging;


import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.messaging.events.StreamEvent;

@Component
@RequiredArgsConstructor
public class EventsEmitter<T extends StreamEvent> {
    private final StreamBridge streamBridge;

    @Async
    public void emit(T event) {
        streamBridge.send(event.destination(), event);
    }
}
