package parcel.delivery.app.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class PdaGatewayAppTest {
    @Test
    @DisplayName("Test main app start")
    void testMain() {
        Assertions.assertDoesNotThrow(() -> PdaGatewayApp.main());
    }
}
