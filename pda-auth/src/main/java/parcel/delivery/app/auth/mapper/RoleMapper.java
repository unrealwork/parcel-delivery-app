package parcel.delivery.app.auth.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.dto.RoleDto;
import parcel.delivery.app.common.mapper.PdaMapperConfig;

@Mapper(config = PdaMapperConfig.class)
public interface RoleMapper extends EntityDtoMapper<Role, RoleDto> {
}
