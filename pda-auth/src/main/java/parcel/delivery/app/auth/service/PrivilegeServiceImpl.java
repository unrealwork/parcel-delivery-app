package parcel.delivery.app.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import parcel.delivery.app.auth.dto.PrivilegeDto;
import parcel.delivery.app.auth.repository.PrivilegeRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    @Override
    public Optional<PrivilegeDto> findByGrantedAuthority(GrantedAuthority authority) {
        return privilegeRepository.findByName(authority.getAuthority());
    }
}
