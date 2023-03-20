package parcel.delivery.app.common.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Import(RoleBasedStrategyTest.Config.class)
@SpringBootTest
class RoleBasedStrategyTest {
    @MockBean
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private TestAggregateStrategy testAggregateStrategy;


    @Test
    @DisplayName("Should select correct strategy for user")
    void testUserStrategy() {
        Mockito.when(authenticationFacade.role())
                .thenReturn(UserRole.USER);
        assertThat(testAggregateStrategy.apply(null), equalTo(UserRole.USER));
    }

    @Test
    @DisplayName("Should select correct strategy for courier")
    void testCourierStrategy() {
        Mockito.when(authenticationFacade.role())
                .thenReturn(UserRole.COURIER);
        assertThat(testAggregateStrategy.apply(null), equalTo(UserRole.COURIER));
    }

    @Test
    @DisplayName("Should throw access denied for not defined strategy")
    void testNotDefinedStrategy() {
        Mockito.when(authenticationFacade.role())
                .thenReturn(UserRole.ADMIN);
        Assertions.assertThrows(AccessDeniedException.class, () -> testAggregateStrategy.apply(null));
    }


    interface TestStrategy extends ComputationRoleBasedStrategy<Void, UserRole> {
        @Override
        default RolePrivilege privilege() {
            return RolePrivilege.BASIC;
        }
    }

    @Configuration
    @Import( {TestAggregateStrategy.class, CourierTestStrategy.class, UserTestStrategy.class})
    static class Config {

    }

    @Service
    static class TestAggregateStrategy extends RoleBasedAggregateStrategy<Void, UserRole, TestStrategy> {

        protected TestAggregateStrategy(List<TestStrategy> strategies, AuthenticationFacade authenticationFacade) {
            super(strategies, authenticationFacade);
        }

        @Override
        public RolePrivilege privilege() {
            return RolePrivilege.BASIC;
        }
    }

    @Component
    static class UserTestStrategy implements TestStrategy {
        @Override
        public UserRole role() {
            return UserRole.USER;
        }

        @Override
        public RolePrivilege privilege() {
            return RolePrivilege.BASIC;
        }

        @Override
        public UserRole apply(Void unused) {
            return UserRole.USER;
        }
    }

    @Component
    static class CourierTestStrategy implements TestStrategy {
        @Override
        public UserRole role() {
            return UserRole.COURIER;
        }

        @Override
        public UserRole apply(Void unused) {
            return UserRole.COURIER;
        }
    }
}
