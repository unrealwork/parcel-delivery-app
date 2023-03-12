package parcel.delivery.app.auth.security.exceptions;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * Exception for case when user is already exist in system.
 *
 * @author unrealwork
 */
public class UserAlreadyExistException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1973765300075432054L;

    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
