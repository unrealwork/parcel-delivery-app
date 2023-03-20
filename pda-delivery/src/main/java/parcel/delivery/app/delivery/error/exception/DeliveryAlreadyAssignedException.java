package parcel.delivery.app.delivery.error.exception;

import parcel.delivery.app.common.error.exception.MessageResolvableException;

public class DeliveryAlreadyAssignedException extends MessageResolvableException {

    public DeliveryAlreadyAssignedException() {
        super(null);
    }

    @Override
    protected String messageCode() {
        return "exception.delivery.already.assigned";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[0];
    }
}
