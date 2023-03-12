package parcel.delivery.app.auth.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.dto.PrivilegeDto;


@Mapper(componentModel = "spring")
public interface PrivilegeMapper extends EntityDtoMapper<Privilege, PrivilegeDto> {
}
