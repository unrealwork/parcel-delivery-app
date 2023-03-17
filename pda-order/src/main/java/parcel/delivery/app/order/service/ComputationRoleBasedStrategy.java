package parcel.delivery.app.order.service;

import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.common.security.core.UserType;

public interface ComputationRoleBasedStrategy<T, S> extends RoleBasedStrategy<T, S> {
    @NotNull
    UserType role();
}
