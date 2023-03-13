package parcel.delivery.app.auth.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.dto.RoleDto;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityDtoMapper<Role, RoleDto> {
}
