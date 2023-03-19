package parcel.delivery.app.delivery.error.exception;

import parcel.delivery.app.common.error.exception.MessageResolvableNotFoundException;

public class CourierNotFoundException extends MessageResolvableNotFoundException {
    private final String userId;

    public CourierNotFoundException(String userId) {
        super(null);
        this.userId = userId;
    }

    @Override
    protected String messageCode() {
        return "exception.courier.not.found";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[] {userId};
    }
}
