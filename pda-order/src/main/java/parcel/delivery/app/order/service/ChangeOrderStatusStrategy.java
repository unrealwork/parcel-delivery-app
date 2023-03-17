package parcel.delivery.app.order.service;

import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;

public interface ChangeOrderStatusStrategy extends ComputationRoleBasedStrategy<OrderRequest<ChangeStatusRequest>, Void> {
    @Override
    default RolePrivilege privilege() {
        return RolePrivilege.CHANGE_ORDER_STATUS;
    }
}
