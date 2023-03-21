package parcel.delivery.app.auth.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.common.security.core.RolePrivilege;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InitRolePrivilegeTest {
    @Autowired
    private PrivilegeRepository privilegeRepository;

    public static Stream<Arguments> testCases() {
        return Arrays.stream(RolePrivilege.values())
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Test that all privileges are initially presented in DB")
    void test(RolePrivilege rolePrivilege) {
        Optional<Privilege> privilege = privilegeRepository.findByName(rolePrivilege.getAuthority());
        assertTrue(privilege.isPresent(),
                () -> "Privilege " + rolePrivilege + " is not presented in DB");
    }
}
