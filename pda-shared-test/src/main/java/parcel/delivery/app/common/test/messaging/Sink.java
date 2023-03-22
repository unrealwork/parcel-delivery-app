package parcel.delivery.app.common.test.messaging;

import org.springframework.stereotype.Component;
import parcel.delivery.app.common.messaging.events.OrderStatusChangedEvent;

import java.util.function.Consumer;

@Component
public class Sink implements Consumer<OrderStatusChangedEvent> {
    @Override
    public void accept(OrderStatusChangedEvent orderStatusChangedEvent) {
        // It's the sink for test purposes to catch produced events
    }
}
