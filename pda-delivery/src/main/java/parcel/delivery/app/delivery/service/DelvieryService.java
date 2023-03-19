package parcel.delivery.app.delivery.service;

import parcel.delivery.app.delivery.domain.DeliveryDto;

import java.util.UUID;

public interface DelvieryService {
    DeliveryDto get(UUID orderId);
}
