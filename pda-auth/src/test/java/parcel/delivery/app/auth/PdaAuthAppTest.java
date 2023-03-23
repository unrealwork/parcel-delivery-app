package parcel.delivery.app.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.delivery.app.common.test.app.BaseAppTest;

class PdaAuthAppTest extends BaseAppTest {

    @Test
    @DisplayName("Should not throw any exceptions during run")
    void testRunningWithMainClass() {
        String arg = "--spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers();
        Assertions.assertDoesNotThrow(() -> PdaAuthApp.main(arg));
    }
}
