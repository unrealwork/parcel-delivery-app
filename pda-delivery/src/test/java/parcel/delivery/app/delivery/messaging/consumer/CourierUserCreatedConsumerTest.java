package parcel.delivery.app.delivery.messaging.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.messaging.events.SignedUpEvent;
import parcel.delivery.app.common.security.core.UserRole;
import parcel.delivery.app.common.test.security.annotations.WithCourierRole;
import parcel.delivery.app.common.test.security.annotations.WithUserRole;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.repository.CourierRepository;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.cloud.stream.output-bindings=userCreatedEvent",
        "spring.cloud.stream.bindings.userCreatedEvent.destination=user-created"
})
@ExtendWith(SpringExtension.class)
class CourierUserCreatedConsumerTest {
    @Autowired
    private CourierUserCreatedConsumer consumer;
    @MockBean
    private CourierRepository courierRepository;

    @Test
    @DisplayName("Should ignore users with non courier role")
    void test() {
        consumer.accept(new SignedUpEvent(UserRole.USER, WithUserRole.USERNAME));
        consumer.accept(new SignedUpEvent(UserRole.COURIER, WithCourierRole.USERNAME));
        verify(courierRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("Should skip event with existing courier")
    void testExistingUser() {
        when(courierRepository.findById(WithCourierRole.USERNAME))
                .thenReturn(Optional.of(Mockito.mock(Courier.class)));
        consumer.accept(new SignedUpEvent(UserRole.COURIER, WithCourierRole.USERNAME));
        verify(courierRepository, times(1)).findById(anyString());
        verify(courierRepository, times(0)).save(any());
    }
}
