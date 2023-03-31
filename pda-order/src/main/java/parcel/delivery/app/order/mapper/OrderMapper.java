package parcel.delivery.app.order.mapper;

import org.mapstruct.Mapper;
import parcel.delivery.app.common.mapper.EntityDtoMapper;
import parcel.delivery.app.common.mapper.PdaMapperConfig;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.dto.OrderDto;

@Mapper(config = PdaMapperConfig.class)
public interface OrderMapper extends EntityDtoMapper<Order, OrderDto> {
}
