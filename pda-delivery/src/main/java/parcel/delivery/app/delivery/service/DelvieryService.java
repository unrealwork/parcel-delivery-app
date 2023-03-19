package parcel.delivery.app.delivery.service;

import parcel.delivery.app.delivery.controller.api.request.AssignCourierRequest;
import parcel.delivery.app.delivery.domain.DeliveryDto;

import java.util.UUID;

public interface DelvieryService {
    DeliveryDto get(UUID orderId);

    void assign(UUID orderId, AssignCourierRequest request);
}
