package parcel.delivery.app.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.delivery.app.common.test.app.BaseAppTest;

class PdaOrderAppTest extends BaseAppTest {

    @Test
    @DisplayName("Should not throw errors when running via main")
    void testMain() {
        String arg = "--spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers();
        Assertions.assertDoesNotThrow(() -> PdaOrderApp.main(arg));
    }
}
