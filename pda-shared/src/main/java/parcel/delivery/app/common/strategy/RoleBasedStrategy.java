package parcel.delivery.app.common.strategy;

import jakarta.validation.constraints.NotNull;
import parcel.delivery.app.common.security.core.RolePrivilege;

import java.util.function.Function;

public interface RoleBasedStrategy<T, S> extends Function<T, S> {

    @NotNull
    RolePrivilege privilege();
}
