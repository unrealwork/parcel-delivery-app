package parcel.delivery.app.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.strategy.ComputationRoleBasedStrategy;
import parcel.delivery.app.delivery.dto.LongLat;
import parcel.delivery.app.delivery.error.exception.DeliveryNotFoundException;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TrackDeliveryStrategy implements ComputationRoleBasedStrategy<UUID, LongLat> {
    private final DeliveryRepository deliveryRepository;

    @Override
    public UserRole role() {
        return UserRole.ADMIN;
    }

    @Override
    public RolePrivilege privilege() {
        return RolePrivilege.TRACK_DELIVERY;
    }

    @Override
    @Transactional
    public LongLat apply(UUID orderId) {
        Optional<LongLat> longLatOpt = deliveryRepository.findCoordinatesByOrderId(orderId);
        if (longLatOpt.isEmpty()) {
            throw new DeliveryNotFoundException(orderId);
        }
        return longLatOpt.get();
    }
}
