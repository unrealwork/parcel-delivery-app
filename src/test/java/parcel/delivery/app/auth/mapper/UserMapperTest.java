package parcel.delivery.app.auth.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.auth.domain.User;
import parcel.delivery.app.auth.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("Test that password is encoding during mapping to entity")
    void testPasswordEncoding() {
        final UserDto userDto = UserDto.builder()
                .clientId("test@mail.com")
                .password("12345678")
                .build();
        User user = userMapper.toEntity(userDto);
        assertNotEquals("Entity password should differ from DTO password value", user.getPassword(), userDto.password());
    }
}
