package parcel.delivery.app.order.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import parcel.delivery.app.common.error.PdaRestExceptionHandler;
import parcel.delivery.app.common.error.model.ErrorResponse;
import parcel.delivery.app.order.error.exception.OrderNotFoundException;

@RequiredArgsConstructor
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrderPdaRestExceptionHandler extends PdaRestExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler( {OrderNotFoundException.class})
    public ErrorResponse handleOrderNotFoundException(OrderNotFoundException e, HttpServletRequest request) {
        return messageResolvableResponse(HttpStatus.NOT_FOUND, request, e.resolvableMessage());
    }
}
