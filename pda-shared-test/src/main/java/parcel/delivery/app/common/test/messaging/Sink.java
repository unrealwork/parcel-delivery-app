package parcel.delivery.app.common.test.messaging;

import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class Sink<T> implements Consumer<T> {
    @Override
    public void accept(T t) {
        // NOOP
    }
}
