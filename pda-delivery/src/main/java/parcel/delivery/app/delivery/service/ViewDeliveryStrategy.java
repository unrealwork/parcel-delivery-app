package parcel.delivery.app.delivery.service;

import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.strategy.ComputationRoleBasedStrategy;
import parcel.delivery.app.delivery.dto.DeliveryDto;

import java.util.UUID;

public interface ViewDeliveryStrategy extends ComputationRoleBasedStrategy<UUID, DeliveryDto> {
    @Override
    default RolePrivilege privilege() {
        return RolePrivilege.VIEW_DELIVERY_DETAILS;
    }
}
