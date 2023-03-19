package parcel.delivery.app.delivery.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.common.mapper.EntityDtoMapper;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeilveryMapper extends EntityDtoMapper<Delivery, DeliveryDto> {
}
