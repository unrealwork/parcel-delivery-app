package parcel.delivery.app.common.security.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import parcel.delivery.app.common.security.error.model.ErrorResponse;
import parcel.delivery.app.common.security.error.model.ValidationErrorResponse;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExecptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(AuthenticationException ex) {
        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception ex) {
        log.warn("Unexpected server error during request", ex);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        var fieldErrors = bindingResult
                .getFieldErrors();
        ValidationErrorResponse validationErrror = new ValidationErrorResponse(status.value(), fieldToErrorMap(fieldErrors, request.getLocale()));
        return ResponseEntity.status(status.value())
                .body(validationErrror);
    }

    private Map<String, String> fieldToErrorMap(List<FieldError> fieldErrors, Locale locale) {

        return fieldErrors.stream()
                .collect(Collectors.toMap(FieldError::getField, oe -> this.extractValidationMessage(oe, locale)));
    }

    private String extractValidationMessage(FieldError fieldError, Locale locale) {
        fieldError.shouldRenderDefaultMessage();
        return Optional.of(fieldError)
                .map(oe -> messageSource.getMessage(oe, locale))
                .orElse("");
    }
}
