package parcel.delivery.app.auth.service.init;

import org.springframework.core.Ordered;

public interface Initializer extends Ordered {
    void init();

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
