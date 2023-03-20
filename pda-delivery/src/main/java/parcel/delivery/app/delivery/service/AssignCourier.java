package parcel.delivery.app.delivery.service;

import java.util.UUID;

public record AssignCourier(UUID orderId, String userId) {
}
