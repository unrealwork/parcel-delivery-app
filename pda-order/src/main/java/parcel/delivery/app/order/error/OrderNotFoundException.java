package parcel.delivery.app.order.error;

import parcel.delivery.app.common.error.exception.MessageResolvableException;

import java.io.Serial;
import java.util.UUID;

/**
 * @author unrealwork
 */
@SuppressWarnings("squid:S2166")
public class OrderNotFoundException extends MessageResolvableException {
    @Serial
    private static final long serialVersionUID = 4197743537759992026L;
    private final UUID uuid;

    public OrderNotFoundException(UUID uuid, Throwable cause) {
        super(cause);
        this.uuid = uuid;
    }

    public OrderNotFoundException(UUID uuid) {
        this(uuid, null);
    }

    @Override
    protected String messageCode() {
        return "exception.order.not.found";
    }

    @Override
    protected Object[] messageArgs() {
        return new Object[] {uuid};
    }
}
