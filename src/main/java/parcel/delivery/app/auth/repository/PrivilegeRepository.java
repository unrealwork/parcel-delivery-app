package parcel.delivery.app.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.dto.PrivilegeDto;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<PrivilegeDto> findByName(String authority);

}
