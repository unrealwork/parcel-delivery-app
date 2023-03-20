package parcel.delivery.app.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parcel.delivery.app.delivery.domain.CourierDto;
import parcel.delivery.app.delivery.mapper.CourierMapper;
import parcel.delivery.app.delivery.repository.CourierRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouriersServiceImpl implements CouriersService {
    private final CourierMapper courierMapper;
    private final CourierRepository courierRepository;

    @Override
    public List<CourierDto> list() {
        return courierMapper.toDto(courierRepository.findAll());
    }
}
