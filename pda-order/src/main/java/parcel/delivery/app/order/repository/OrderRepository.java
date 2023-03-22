package parcel.delivery.app.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parcel.delivery.app.common.domain.OrderStatus;
import parcel.delivery.app.order.domain.Order;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByCreatedByEqualsIgnoreCase(String createdBy);

    List<Order> findAllByAssignedToEqualsIgnoreCase(String assignedTo);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.status = :status where o.id = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") OrderStatus status);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.destination = :destination where o.id = :id")
    void updateDestination(@Param("id") UUID id, @Param("destination") String destination);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.status = :status, o.assignedTo = :courier where o.id = :id")
    void updateStatusAndCourier(@Param("id") UUID id, @Param("status") OrderStatus status,
                                @Param("courier") String courier);
}
