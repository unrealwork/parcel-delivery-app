package parcel.delivery.app.delivery.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.common.mapper.EntityDtoMapper;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierDto;

@Mapper(componentModel = "spring")
public interface CourierMapper extends EntityDtoMapper<Courier, CourierDto> {
}
