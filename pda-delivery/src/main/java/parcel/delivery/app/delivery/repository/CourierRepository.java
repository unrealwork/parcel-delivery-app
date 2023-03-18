package parcel.delivery.app.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parcel.delivery.app.delivery.domain.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, String> {
}
