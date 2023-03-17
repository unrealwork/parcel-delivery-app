package parcel.delivery.app.auth.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.util.TestUtil;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static parcel.delivery.app.auth.util.TestUtil.arrayOf;
import static parcel.delivery.app.common.security.core.RolePrivilege.CANCEL_ORDER;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_DESTINATION;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_ORDER_STATUS;
import static parcel.delivery.app.common.security.core.RolePrivilege.CREATE_COURIER_USER;
import static parcel.delivery.app.common.security.core.RolePrivilege.CREATE_ORDER;
import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_ORDERS;
import static parcel.delivery.app.common.security.core.UserType.ADMIN;
import static parcel.delivery.app.common.security.core.UserType.COURIER;
import static parcel.delivery.app.common.security.core.UserType.USER;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class InitRolesTest {
    @Autowired
    private RoleRepository roleRepository;

    public static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(USER, TestUtil.arrayOf(VIEW_ORDERS, CREATE_ORDER, CANCEL_ORDER, CHANGE_DESTINATION)),
                Arguments.of(ADMIN, arrayOf(CREATE_COURIER_USER, CHANGE_ORDER_STATUS)),
                Arguments.of(COURIER, TestUtil.<RolePrivilege>arrayOf())

        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    @DisplayName("Test that all roles have valid privileges")
    void test(UserType userType, RolePrivilege... privileges) {
        Optional<Role> roleOpt = roleRepository.findByName(userType.getAuthority());
        assertTrue(roleOpt.isPresent(),
                () -> "Useer with role " + userType.getAuthority() + " should be presented initially int DB");

        final Role role = roleOpt.get();
        final Collection<RolePrivilege> rolePrivileges = role.getPrivileges()
                .stream()
                .map(Privilege::getName)
                .map(RolePrivilege::fromAuthority)
                .collect(Collectors.toSet());
        String assertMessage = TestUtil.format("Role {} should have privileges {}",
                userType.getAuthority(),
                Arrays.toString(privileges));
        assertThat(assertMessage, rolePrivileges, containsInAnyOrder(privileges));
    }

}
