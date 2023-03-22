package parcel.delivery.app.delivery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.delivery.app.common.test.messaging.BaseIntegreationTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PdaDeliveryAppTest extends BaseIntegreationTest {

    @Test
    @DisplayName("Should run main class without errors")
    void testMain() {
        String arg = "--spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers();
        assertDoesNotThrow(() -> PdaDeliveryApp.main(arg));
    }
}
