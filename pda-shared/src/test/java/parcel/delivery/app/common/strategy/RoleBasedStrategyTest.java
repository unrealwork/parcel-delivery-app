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
import parcel.delivery.app.common.security.core.UserType;

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
                .thenReturn(UserType.USER);
        assertThat(testAggregateStrategy.apply(null), equalTo(UserType.USER));
    }

    @Test
    @DisplayName("Should select correct strategy for courier")
    void testCourierStrategy() {
        Mockito.when(authenticationFacade.role())
                .thenReturn(UserType.COURIER);
        assertThat(testAggregateStrategy.apply(null), equalTo(UserType.COURIER));
    }

    @Test
    @DisplayName("Should throw access denied for not defined strategy")
    void testNotDefinedStrategy() {
        Mockito.when(authenticationFacade.role())
                .thenReturn(UserType.ADMIN);
        Assertions.assertThrows(AccessDeniedException.class, () -> testAggregateStrategy.apply(null));
    }


    interface TestStrategy extends ComputationRoleBasedStrategy<Void, UserType> {
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
    static class TestAggregateStrategy extends RoleBasedAggregateStrategy<Void, UserType, TestStrategy> {

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
        public UserType role() {
            return UserType.USER;
        }

        @Override
        public RolePrivilege privilege() {
            return RolePrivilege.BASIC;
        }

        @Override
        public UserType apply(Void unused) {
            return UserType.USER;
        }
    }

    @Component
    static class CourierTestStrategy implements TestStrategy {
        @Override
        public UserType role() {
            return UserType.COURIER;
        }

        @Override
        public UserType apply(Void unused) {
            return UserType.COURIER;
        }
    }
}
