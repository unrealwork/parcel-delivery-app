package parcel.delivery.app.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parcel.delivery.app.delivery.domain.DeliveryDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DelvieryServiceImpl implements DelvieryService {
    private final ViewDelvieryAggregateStrategy viewDelvieryAggregateStrategy;

    @Override
    public DeliveryDto get(UUID orderId) {
        return viewDelvieryAggregateStrategy.apply(orderId);
    }
}
