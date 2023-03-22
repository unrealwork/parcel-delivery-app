package parcel.delivery.app.common.test.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.common.test.client.ApiRestClient;
import parcel.delivery.app.common.test.messaging.BaseIntegreationTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class BaseControllerTest extends BaseIntegreationTest {
    @Autowired
    protected ApiRestClient client;
}
