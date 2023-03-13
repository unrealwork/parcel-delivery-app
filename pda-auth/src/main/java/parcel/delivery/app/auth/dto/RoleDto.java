package parcel.delivery.app.auth.dto;

import java.util.Collection;

public record RoleDto(Long id, String name, Collection<PrivilegeDto> privileges) {
}
