package parcel.delivery.app.delivery.error;

import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import parcel.delivery.app.common.error.PdaRestExceptionHandler;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PdaDeliveryExceptionHandler extends PdaRestExceptionHandler {
    public PdaDeliveryExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}
