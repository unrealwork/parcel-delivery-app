package parcel.delivery.app.delivery.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class DeliveryRepositoryTest {
    private static final UUID ORDER_ID = UUID.fromString("4843a6c6-28e7-4a85-8f8d-f7e343389350");

    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("Should save entity with provided id")
    void testSave() {
        Courier courier = Courier.builder()
                .userId(WithCourierRole.USERNAME)
                .status(CourierStatus.AVAILABLE)
                .build();
        courierRepository.saveAndFlush(courier);
        Delivery delivery = Delivery.builder()
                .orderId(ORDER_ID)
                .status(DeliveryStatus.INITIAL)
                .orderedBy(WithUserRole.USERNAME)
                .courier(courier)
                .build();
        Delivery savedDelivery = deliveryRepository.saveAndFlush(delivery);
        assertThat(savedDelivery.getOrderId(), equalTo(ORDER_ID));
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        deliveryRepository.deleteAll();
        courierRepository.deleteAll();
    }
}
