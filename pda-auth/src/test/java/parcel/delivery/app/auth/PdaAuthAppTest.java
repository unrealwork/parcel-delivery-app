package parcel.delivery.app.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PdaAuthAppTest {

    @Test
    @DisplayName("Should not throw any exceptions during run")
    void testRunningWithMainClass() {
        Assertions.assertDoesNotThrow(() -> PdaAuthApp.main());
    }
}
