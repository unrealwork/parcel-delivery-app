package parcel.delivery.app.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parcel.delivery.app.order.domain.Order;
import parcel.delivery.app.order.domain.OrderStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByCreatedByEqualsIgnoreCase(String createdBy);

    @Modifying
    @Query("update Order o set o.status = :status where o.id = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") OrderStatus status);
}
