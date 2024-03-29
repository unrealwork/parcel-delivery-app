package parcel.delivery.app.common.error.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;

public abstract class MessageResolvableException extends RuntimeException {
    protected MessageResolvableException(Throwable cause) {
        super(cause);
    }

    protected abstract String messageCode();

    protected abstract Object[] messageArgs();

    public MessageSourceResolvable resolvableMessage() {
        return new DefaultMessageSourceResolvable(new String[] {messageCode()}, messageArgs());
    }
}
