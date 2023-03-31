package parcel.delivery.app.auth.mapper;

import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.dto.PrivilegeDto;
import parcel.delivery.app.common.mapper.annotations.PdaMapper;


@PdaMapper
public interface PrivilegeMapper extends EntityDtoMapper<Privilege, PrivilegeDto> {
}
