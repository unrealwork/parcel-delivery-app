package parcel.delivery.app.auth.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.repository.RoleRepository;
import parcel.delivery.app.auth.security.core.UserType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public final class UserTypeRolesMapper {
    private final RoleRepository roleRepository;

    public Collection<Role> map(UserType value) {
        Optional<Role> role = roleRepository.findByName(value.name());
        return List.of(role.orElseThrow(() -> new IllegalStateException("Unknown role: Role should be presented in DB")));
    }
}
