package parcel.delivery.app.delivery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PdaDeliveryAppTest {

    @Test
    @DisplayName("Should run main class without errors")
    void testMain() {
        assertDoesNotThrow(() -> PdaDeliveryApp.main());
    }
}
