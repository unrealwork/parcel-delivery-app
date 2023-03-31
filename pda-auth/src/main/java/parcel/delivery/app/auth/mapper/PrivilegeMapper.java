package parcel.delivery.app.auth.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.dto.PrivilegeDto;
import parcel.delivery.app.common.mapper.PdaMapperConfig;


@Mapper(config = PdaMapperConfig.class)
public interface PrivilegeMapper extends EntityDtoMapper<Privilege, PrivilegeDto> {
}
