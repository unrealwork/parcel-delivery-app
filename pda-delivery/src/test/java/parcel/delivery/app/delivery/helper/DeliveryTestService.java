package parcel.delivery.app.delivery.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class DeliveryTestService {
    private final DeliveryRepository deliveryRepository;


    public Delivery save(Delivery delivery) {
        return deliveryRepository.save(delivery.toBuilder()
                .build());
    }


    public void deleteAll() {
        deliveryRepository.deleteAll();
    }
}
