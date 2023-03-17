package parcel.delivery.app.order.service;

import org.springframework.stereotype.Service;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;

import java.util.List;

@Service
public class ChangeOrderStatusAggregateStrategy extends RoleBasedAggregateStrategy<OrderRequest<ChangeStatusRequest>, Void, ChangeOrderStatusStrategy> {
    protected ChangeOrderStatusAggregateStrategy(List<ChangeOrderStatusStrategy> strategies, AuthenticationFacade authenticationFacade) {
        super(strategies, authenticationFacade);
    }

    @Override
    public RolePrivilege privilege() {
        return RolePrivilege.CHANGE_ORDER_STATUS;
    }
}
