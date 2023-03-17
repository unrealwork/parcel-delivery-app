package parcel.delivery.app.common.error.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.AccessDeniedException;


public abstract class MessageResolvableAccessDeniedException extends AccessDeniedException {
    private final DefaultMessageSourceResolvable resolvableMessage = new DefaultMessageSourceResolvable(new String[] {messageCode()}, messageArgs());

    protected MessageResolvableAccessDeniedException(Throwable cause) {
        super(null, cause);
    }

    protected abstract String messageCode();

    protected abstract Object[] messageArgs();

    public MessageSourceResolvable resolvableMessage() {
        return resolvableMessage;
    }
}
