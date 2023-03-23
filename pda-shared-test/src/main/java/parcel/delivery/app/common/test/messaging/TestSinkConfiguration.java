package parcel.delivery.app.common.test.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import parcel.delivery.app.common.messaging.events.DeliveryAcceptedEvent;
import parcel.delivery.app.common.messaging.events.DeliveryAssignedEvent;
import parcel.delivery.app.common.messaging.events.OrderCreatedEvent;
import parcel.delivery.app.common.messaging.events.OrderStatusChangedEvent;
import parcel.delivery.app.common.messaging.events.SignedUpEvent;

@Configuration
public class TestSinkConfiguration {
    @Bean
    public Sink<DeliveryAcceptedEvent> deliveryAcceptedSink() {
        return new Sink<>();
    }

    @Bean
    public Sink<DeliveryAssignedEvent> deliveryAssignedSink() {
        return new Sink<>();
    }

    @Bean
    public Sink<SignedUpEvent> userCreatedSink() {
        return new Sink<>();
    }

    @Bean
    Sink<OrderStatusChangedEvent> orderStatusChangedSink() {
        return new Sink<>();
    }

    @Bean
    Sink<OrderCreatedEvent> orderCreatedSink() {
        return new Sink<>();
    }
}

