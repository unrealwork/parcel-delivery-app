package parcel.delivery.app.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parcel.delivery.app.delivery.domain.Courier;
import parcel.delivery.app.delivery.domain.CourierStatus;

@Repository
public interface CourierRepository extends JpaRepository<Courier, String> {

    @Modifying(flushAutomatically = true)
    @Query("update Courier c set c.status = :status where c.userId = :id")
    void updateStatus(@Param("id") String userId, @Param("status") CourierStatus status);
}
