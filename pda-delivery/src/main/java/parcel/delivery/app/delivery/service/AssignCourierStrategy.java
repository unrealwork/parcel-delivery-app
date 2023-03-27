package parcel.delivery.app.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.error.exception.CourierNotFoundException;
import parcel.delivery.app.delivery.error.exception.DeliveryAlreadyAssignedException;
import parcel.delivery.app.delivery.error.exception.DeliveryNotFoundException;
import parcel.delivery.app.delivery.repository.CourierRepository;
import parcel.delivery.app.delivery.repository.DeliveryRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AssignCourierStrategy implements Function<AssignCourier, Void> {
    private final CourierRepository courierRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional
    public Void apply(AssignCourier assignCourier) {
        UUID orderId = assignCourier.orderId();
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(orderId);
        if (deliveryOpt.isEmpty()) {
            throw new DeliveryNotFoundException(orderId);
        }
        Delivery delivery = deliveryOpt.get();
        String userId = assignCourier.userId();
        Optional<Courier> courierOpt = courierRepository.findById(userId);
        if (courierOpt.isEmpty()) {
            throw new CourierNotFoundException(userId);
        }
        if (delivery.getCourier() != null) {
            throw new DeliveryAlreadyAssignedException();
        }
        Courier courier = courierOpt.get();
        courier.setStatus(CourierStatus.UNAVAILABLE);
        delivery.setCourier(courier);
        deliveryRepository.save(delivery);
        return null;
    }
}
