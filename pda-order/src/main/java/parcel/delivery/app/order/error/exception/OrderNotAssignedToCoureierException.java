package parcel.delivery.app.order.error.exception;

import parcel.delivery.app.common.error.exception.MessageResolvableAccessDeniedException;

public class OrderNotAssignedToCoureierException extends MessageResolvableAccessDeniedException {
    protected OrderNotAssignedToCoureierException(Throwable cause) {
        super(cause);
    }

    public OrderNotAssignedToCoureierException() {
        this(null);
    }

    @Override
    protected String messageCode() {
        return "exception.order.not.assigned";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[0];
    }
}
