package parcel.delivery.app.order.error.exception;

import parcel.delivery.app.common.error.exception.MessageResolvableException;

import java.io.Serial;

public class OrderDestinationModificationException extends MessageResolvableException {
    @Serial
    private static final long serialVersionUID = -7300402946087348827L;

    public OrderDestinationModificationException(Throwable cause) {
        super(cause);
    }


    public OrderDestinationModificationException() {
        this(null);
    }

    @Override
    protected String messageCode() {
        return "exception.order.change.destination";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[0];
    }
}
