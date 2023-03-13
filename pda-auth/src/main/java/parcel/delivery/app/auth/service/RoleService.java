package parcel.delivery.app.auth.service;

import org.springframework.lang.NonNull;
import parcel.delivery.app.auth.dto.RoleDto;

import java.util.Optional;

public interface RoleService {
    Optional<RoleDto> findRoleByAuthority(@NonNull String authority);
}
