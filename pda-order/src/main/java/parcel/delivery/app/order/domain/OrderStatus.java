package parcel.delivery.app.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;

@RequiredArgsConstructor
public enum OrderStatus implements Ordered {
    INITIAL(0),
    ACCEPTED(1),
    PENDING(2),
    IN_PROGRESS(3),
    DELIVERED(4),
    CANCELLED(5);

    private final int order;

    @Override
    public int getOrder() {
        return order;
    }
}
