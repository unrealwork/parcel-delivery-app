package parcel.delivery.app.delivery.repository;

import org.hamcrest.CoreMatchers;
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
import parcel.delivery.app.delivery.dto.LongLat;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.DELIVERY;
import static parcel.delivery.app.delivery.helper.DeliveryDomainConstants.ORDER_ID;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class DeliveryRepositoryTest {
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
                .latitude(BigDecimal.ONE)
                .latitude(BigDecimal.ONE)
                .orderedBy(WithUserRole.USERNAME)
                .courier(courier)
                .build();
        Delivery savedDelivery = deliveryRepository.saveAndFlush(delivery);
        assertThat(savedDelivery.getOrderId(), equalTo(ORDER_ID));
        assertThat(savedDelivery.getCourier(), CoreMatchers.notNullValue());
        assertThat(savedDelivery.getStatus(), equalTo(DeliveryStatus.INITIAL));
        assertThat(savedDelivery.getCreatedAt(), CoreMatchers.notNullValue());
        assertThat(savedDelivery.getUpdatedAt(), CoreMatchers.notNullValue());
    }

    @Test
    @DisplayName("Should load coordinates via JPA projection")
    void testFindCoordinates() {
        deliveryRepository.saveAndFlush(DELIVERY);
        Optional<LongLat> longLatOpt = deliveryRepository.findCoordinatesByOrderId(ORDER_ID);
        assertTrue(longLatOpt.isPresent());
        LongLat longLat = longLatOpt.get();
        assertThat(longLat.getLongitude(), CoreMatchers.notNullValue());
        assertThat(longLat.getLatitude(), CoreMatchers.notNullValue());
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        deliveryRepository.deleteAll();
        courierRepository.deleteAll();
    }
}
