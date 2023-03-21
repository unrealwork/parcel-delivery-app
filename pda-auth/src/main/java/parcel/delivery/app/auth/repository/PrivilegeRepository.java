package parcel.delivery.app.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import parcel.delivery.app.auth.domain.Privilege;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByName(String authority);

    boolean existsByName(String name);
}
