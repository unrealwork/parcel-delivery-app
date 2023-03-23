package parcel.delivery.app.delivery.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class DeliveryTestService {
    private final DeliveryRepository deliveryRepository;


    public Delivery save(Delivery delivery) {
        return deliveryRepository.save(delivery.toBuilder()
                .build());
    }

    public void updateStatus(UUID orderId, DeliveryStatus status) {
        deliveryRepository.updateStatus(orderId, status);
    }

    @Transactional(readOnly = true)
    public Optional<Delivery> findById(UUID orderID) {
        return deliveryRepository.findById(orderID);
    }


    public void deleteAll() {
        deliveryRepository.deleteAll();
    }
}
