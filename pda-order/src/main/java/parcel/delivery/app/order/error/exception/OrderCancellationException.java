package parcel.delivery.app.order.error.exception;

import parcel.delivery.app.common.error.exception.MessageResolvableException;

public class OrderCancellationException extends MessageResolvableException {
    protected OrderCancellationException(Throwable cause) {
        super(cause);
    }

    public OrderCancellationException() {
        this(null);
    }

    @Override
    protected String messageCode() {
        return "exception.order.cancel";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[0];
    }
}
