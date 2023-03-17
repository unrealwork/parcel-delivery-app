package parcel.delivery.app.order.controller.api.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeOrderDestinationRequest(@NotBlank String destination) {
}
