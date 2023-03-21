package parcel.delivery.app.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parcel.delivery.app.auth.domain.User;

import java.util.Optional;

/**
 * Data repository for {@link User} domain objects.
 *
 * @author unrealwork
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClientIdEqualsIgnoreCase(String clientId);

    boolean existsByClientId(String clientId);
}
