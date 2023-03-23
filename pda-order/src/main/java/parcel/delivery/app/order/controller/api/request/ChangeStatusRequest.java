package parcel.delivery.app.order.controller.api.request;

import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.common.domain.OrderStatus;

public record ChangeStatusRequest(@NotNull OrderStatus status) {
}
