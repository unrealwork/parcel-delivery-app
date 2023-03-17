package parcel.delivery.app.order.service;

import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.order.dto.OrderDto;

import java.util.List;

public interface ViewOrdersStrategy extends ComputationRoleBasedStrategy<Void, List<OrderDto>> {
    @Override
    default RolePrivilege privilege() {
        return RolePrivilege.VIEW_ORDERS;
    }
}
