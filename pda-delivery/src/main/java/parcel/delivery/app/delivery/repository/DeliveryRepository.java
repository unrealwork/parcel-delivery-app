package parcel.delivery.app.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.dto.LongLat;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<LongLat> findCoordinatesByOrderId(UUID orderId);
}
