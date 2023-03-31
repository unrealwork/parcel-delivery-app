package parcel.delivery.app.auth.mapper;

import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.dto.RoleDto;
import parcel.delivery.app.common.mapper.annotations.PdaMapper;

@PdaMapper
public interface RoleMapper extends EntityDtoMapper<Role, RoleDto> {
}
