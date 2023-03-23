package parcel.delivery.app.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parcel.delivery.app.delivery.domain.Delivery;
import parcel.delivery.app.delivery.domain.DeliveryStatus;
import parcel.delivery.app.delivery.dto.LongLat;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<LongLat> findCoordinatesByOrderId(UUID orderId);

    @Modifying(flushAutomatically = true)
    @Query("update Delivery d set d.status = :status where d.orderId = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") DeliveryStatus status);
}
