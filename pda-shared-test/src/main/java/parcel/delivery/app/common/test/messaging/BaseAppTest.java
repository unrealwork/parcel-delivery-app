package parcel.delivery.app.common.test.messaging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseAppTest extends BaseIntegreationTest {

    public static final String SPRING_KAFKA_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    @BeforeEach
    void setup() {
        System.getenv()
                .put(SPRING_KAFKA_BOOTSTRAP_SERVERS, kafka.getBootstrapServers());
    }

    @AfterEach
    void cleanup() {
        System.getenv()
                .remove(SPRING_KAFKA_BOOTSTRAP_SERVERS);
    }
}
