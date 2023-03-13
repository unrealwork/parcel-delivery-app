package parcel.delivery.app.auth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.dto.PrivilegeDto;

import java.util.Optional;

public interface PrivilegeService {
    @Transactional(readOnly = true)
    Optional<PrivilegeDto> findByGrantedAuthority(GrantedAuthority authority);
}
