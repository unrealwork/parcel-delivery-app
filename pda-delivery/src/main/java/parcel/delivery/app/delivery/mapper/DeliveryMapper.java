package parcel.delivery.app.delivery.mapper;

import parcel.delivery.app.common.mapper.EntityDtoMapper;
import parcel.delivery.app.common.mapper.annotations.PdaMapper;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.dto.DeliveryDto;

@PdaMapper
public interface DeliveryMapper extends EntityDtoMapper<Delivery, DeliveryDto> {
}
