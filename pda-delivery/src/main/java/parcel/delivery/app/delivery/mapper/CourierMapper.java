package parcel.delivery.app.delivery.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.common.mapper.EntityDtoMapper;
import parcel.delivery.app.common.mapper.PdaMapperConfig;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.dto.CourierDto;

@Mapper(config = PdaMapperConfig.class)
public interface CourierMapper extends EntityDtoMapper<Courier, CourierDto> {
}
