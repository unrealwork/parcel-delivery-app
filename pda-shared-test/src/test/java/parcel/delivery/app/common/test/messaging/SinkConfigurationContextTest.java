package parcel.delivery.app.common.test.messaging;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Import( {TestSinkConfiguration.class})
class SinkConfigurationContextTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Should contain sinks required for event consuming")
    void test() {
        Set<String> beanDefinitionNames = Set.of(applicationContext.getBeanDefinitionNames());
        assertThat(beanDefinitionNames, CoreMatchers.hasItems(
                "userCreatedSink", "orderCreatedSink", "orderStatusChangedSink",
                "deliveryAcceptedSink", "deliveryAssignedSink"
        ));
    }

    @Configuration
    static class Config {

    }
}
