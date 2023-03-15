package parcel.delivery.app.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PdaOrderAppTest {

    @Test
    @DisplayName("Should not throw errors when running via main")
    void testMain() {
        Assertions.assertDoesNotThrow(() -> PdaOrderApp.main());
    }
}
