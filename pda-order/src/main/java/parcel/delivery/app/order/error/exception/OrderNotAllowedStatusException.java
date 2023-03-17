package parcel.delivery.app.order.error.exception;

import parcel.delivery.app.common.error.exception.MessageResolvableException;
import parcel.delivery.app.order.domain.OrderStatus;

public class OrderNotAllowedStatusException extends MessageResolvableException {
    private final OrderStatus fromStatus;
    private final OrderStatus toStatus;

    public OrderNotAllowedStatusException(OrderStatus fromStatus, OrderStatus toStatus) {
        super(null);
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }


    @Override
    protected String messageCode() {
        return "exception.order.status.change.not.allowed";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[] {fromStatus, toStatus};
    }
}
