package parcel.delivery.app.delivery.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.repository.CourierRepository;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class CourierTestService {
    private final CourierRepository courierRepository;


    public Courier save(Courier courier) {
        return courierRepository.saveAndFlush(courier.toBuilder()
                .build());
    }


    public void deleteAll() {
        courierRepository.deleteAll();
    }

    public Optional<Courier> findById(String userId) {
        return courierRepository.findById(userId);
    }
}
