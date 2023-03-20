package parcel.delivery.app.auth.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.repository.RoleRepository;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserTypeRolesMapper {
    private final RoleRepository roleRepository;

    public Collection<Role> map(UserRole value) {
        Optional<Role> role = roleRepository.findByName(value.name());
        return List.of(role.orElseThrow(() -> new IllegalStateException("Unknown role: Role should be presented in DB")));
    }
}
