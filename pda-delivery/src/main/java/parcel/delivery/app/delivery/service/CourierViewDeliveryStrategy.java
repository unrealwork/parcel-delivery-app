package parcel.delivery.app.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.UserType;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryDto;
import parcel.delivery.app.delivery.error.exception.DeilveryAcccessDeniedException;
import parcel.delivery.app.delivery.error.exception.DeliveryNotFoundException;
import parcel.delivery.app.delivery.mapper.DeilveryMapper;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CourierViewDeliveryStrategy implements ViewDeliveryStrategy {
    private final DeliveryRepository deliveryRepository;
    private final DeilveryMapper deilveryMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserType role() {
        return UserType.COURIER;
    }

    @Override
    @Transactional
    public DeliveryDto apply(UUID orderId) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(orderId);
        if (deliveryOpt.isEmpty()) {
            throw new DeliveryNotFoundException(orderId);
        }
        Delivery delivery = deliveryOpt.get();
        String username = authenticationFacade.username();
        Courier courier = delivery.getCourier();
        if (courier == null || !Objects.equals(courier
                .getUserId(), username)) {
            throw new DeilveryAcccessDeniedException();
        }
        return deilveryMapper.toDto(delivery);
    }
}
