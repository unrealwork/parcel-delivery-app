package parcel.delivery.app.delivery.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.messaging.events.SignedUpEvent;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;
import parcel.delivery.app.delivery.repository.CourierRepository;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class CourierUserCreatedConsumer implements Consumer<SignedUpEvent> {
    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public void accept(SignedUpEvent signedUpEvent) {
        if (signedUpEvent.userRole() == UserRole.COURIER) {
            String userId = signedUpEvent.userId();
            Optional<Courier> courier = courierRepository.findById(userId);
            if (courier.isEmpty()) {
                Courier newCourier = Courier.builder()
                        .userId(userId)
                        .status(CourierStatus.AVAILABLE)
                        .build();
                courierRepository.save(newCourier);
                log.debug("Creating new courier {}", newCourier);
            }
        }
    }
}
