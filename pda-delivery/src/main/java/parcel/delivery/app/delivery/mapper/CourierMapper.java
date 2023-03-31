package parcel.delivery.app.delivery.mapper;

import parcel.delivery.app.common.mapper.EntityDtoMapper;
import parcel.delivery.app.common.mapper.annotations.PdaMapper;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.dto.CourierDto;

@PdaMapper
public interface CourierMapper extends EntityDtoMapper<Courier, CourierDto> {
}
