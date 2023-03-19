package parcel.delivery.app.delivery.service;

import org.springframework.stereotype.Component;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.strategy.RoleBasedAggregateStrategy;
import parcel.delivery.app.delivery.domain.DeliveryDto;

import java.util.List;
import java.util.UUID;

@Component
public class ViewDelvieryAggregateStrategy extends RoleBasedAggregateStrategy<UUID, DeliveryDto, ViewDeliveryStrategy> {
    protected ViewDelvieryAggregateStrategy(List<ViewDeliveryStrategy> strategies,
                                            AuthenticationFacade authenticationFacade) {
        super(strategies, authenticationFacade);
    }

    @Override
    public RolePrivilege privilege() {
        return RolePrivilege.VIEW_DELIVERY_DETAILS;
    }
}
