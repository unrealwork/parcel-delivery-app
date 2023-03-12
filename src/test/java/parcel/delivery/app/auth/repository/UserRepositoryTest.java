package parcel.delivery.app.auth.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save user to DB and validate its presence by clientId")
    void testSave() {
        final User user = User.builder()
                .clientId("test@mail.com")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .build();
        userRepository.save(user);
        Optional<User> useByClientId = userRepository.findByClientIdEqualsIgnoreCase(user.getClientId());
        assertTrue(useByClientId.isPresent(),
                () -> user + " should be found by clientId=" + user.getClientId());
    }
}
