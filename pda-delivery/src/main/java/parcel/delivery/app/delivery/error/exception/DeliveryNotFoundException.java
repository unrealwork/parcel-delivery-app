package parcel.delivery.app.delivery.error.exception;

import parcel.delivery.app.common.error.exception.MessageResolvableNotFoundException;

import java.io.Serial;
import java.util.UUID;

public class DeliveryNotFoundException extends MessageResolvableNotFoundException {
    @Serial
    private static final long serialVersionUID = 130923824354487611L;
    private final UUID oderId;

    public DeliveryNotFoundException(UUID oderId) {
        super(null);
        this.oderId = oderId;
    }

    @Override
    protected String messageCode() {
        return "exception.delivery.not.found";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[] {oderId};
    }
}
