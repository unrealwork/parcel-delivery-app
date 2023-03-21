package parcel.delivery.app.auth.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.util.TestUtil;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static parcel.delivery.app.common.security.core.UserRole.ADMIN;
import static parcel.delivery.app.common.security.core.UserRole.COURIER;
import static parcel.delivery.app.common.security.core.UserRole.USER;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class InitRolesTest {
    @Autowired
    private RoleRepository roleRepository;

    public static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(USER),
                Arguments.of(ADMIN),
                Arguments.of(COURIER)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Test that all roles have valid privileges")
    void test(UserRole userRole) {
        Optional<Role> roleOpt = roleRepository.findByName(userRole.getAuthority());
        assertTrue(roleOpt.isPresent(),
                () -> "Useer with role " + userRole.getAuthority() + " should be presented initially int DB");

        final Role role = roleOpt.get();
        final Collection<RolePrivilege> rolePrivileges = role.getPrivileges()
                .stream()
                .map(Privilege::getName)
                .map(RolePrivilege::fromAuthority)
                .collect(Collectors.toSet());
        String assertMessage = TestUtil.format("Role {} should have privileges {}",
                userRole.getAuthority(),
                userRole.privileges());
        assertThat(assertMessage, rolePrivileges, equalTo(userRole.privileges()));
    }

}
