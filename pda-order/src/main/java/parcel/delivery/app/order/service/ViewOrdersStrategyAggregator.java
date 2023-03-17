package parcel.delivery.app.order.service;

import org.springframework.stereotype.Service;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.order.dto.OrderDto;

import java.util.List;


@Service
public class ViewOrdersStrategyAggregator extends RoleBasedStrategyAggregator<Void, List<OrderDto>, ViewOrdersStrategy> {
    protected ViewOrdersStrategyAggregator(List<ViewOrdersStrategy> strategies, AuthenticationFacade authenticationFacade) {
        super(strategies, authenticationFacade);
    }

    @Override
    public RolePrivilege privilege() {
        return RolePrivilege.VIEW_ORDERS;
    }
}
