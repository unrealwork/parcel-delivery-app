package parcel.delivery.app.delivery.controller.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AssignCourierRequest(@NotNull @Email String courierId) {
}
