package parcel.delivery.app.auth.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import parcel.delivery.app.auth.annotations.mapper.PasswordEncodingMapping;

@RequiredArgsConstructor
@Component
public class PasswordEncoderMapper {
    private final PasswordEncoder pEncoder;

    @PasswordEncodingMapping
    public String map(String password) {
        return pEncoder.encode(password);
    }
}
