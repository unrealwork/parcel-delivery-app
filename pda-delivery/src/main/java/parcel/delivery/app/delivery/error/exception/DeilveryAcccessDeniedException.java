package parcel.delivery.app.delivery.error.exception;


import parcel.delivery.app.common.error.exception.MessageResolvableAccessDeniedException;

public class DeilveryAcccessDeniedException extends MessageResolvableAccessDeniedException {
    public DeilveryAcccessDeniedException() {
        super(null);
    }

    @Override
    protected String messageCode() {
        return "exception.delivery.access.denied";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[0];
    }
}
