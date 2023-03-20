package parcel.delivery.app.delivery.service;

import parcel.delivery.app.delivery.controller.api.request.AssignCourierRequest;
import parcel.delivery.app.delivery.dto.DeliveryDto;
import parcel.delivery.app.delivery.dto.LongLat;

import java.util.UUID;

public interface DelvieryService {
    DeliveryDto get(UUID orderId);

    void assign(UUID orderId, AssignCourierRequest request);

    LongLat track(UUID orderId);
}
