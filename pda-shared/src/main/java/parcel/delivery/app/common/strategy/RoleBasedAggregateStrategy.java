package parcel.delivery.app.common.strategy;

import org.springframework.security.access.AccessDeniedException;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.EnumMap;
import java.util.List;


public abstract class RoleBasedAggregateStrategy<T, S, C extends ComputationRoleBasedStrategy<T, S>> implements RoleBasedStrategy<T, S> {
    private final EnumMap<UserRole, C> strategiesMap;
    private final AuthenticationFacade authenticationFacade;
    private final UserRole defaultRole;

    protected RoleBasedAggregateStrategy(List<C> strategies, AuthenticationFacade authenticationFacade, UserRole defaultRole) {
        this.authenticationFacade = authenticationFacade;
        this.defaultRole = defaultRole;
        strategiesMap = new EnumMap<>(UserRole.class);
        for (C strategy : strategies) {
            if (strategy.privilege() != this.privilege()) {
                throw new IllegalStateException("Only strategies with privilege " + privilege() + " are supported");
            }
            strategiesMap.put(strategy.role(), strategy);
        }
    }

    @Override
    public S apply(T t) {
        final UserRole role = authenticationFacade.role() == UserRole.SUPERUSER ? defaultRole : authenticationFacade.role();
        ComputationRoleBasedStrategy<T, S> strategy = strategiesMap.get(role);
        if (strategy == null) {
            throw new AccessDeniedException(privilege() + " does not have strategy for " + role);
        }
        return strategy.apply(t);
    }
}
