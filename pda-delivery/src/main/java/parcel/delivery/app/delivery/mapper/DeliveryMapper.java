package parcel.delivery.app.delivery.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.common.mapper.EntityDtoMapper;
import parcel.delivery.app.common.mapper.PdaMapperConfig;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.dto.DeliveryDto;

@Mapper(config = PdaMapperConfig.class)
public interface DeliveryMapper extends EntityDtoMapper<Delivery, DeliveryDto> {
}
