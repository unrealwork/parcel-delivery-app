package parcel.delivery.app.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.dto.RoleDto;
import parcel.delivery.app.auth.mapper.RoleMapper;
import parcel.delivery.app.auth.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public Optional<RoleDto> findRoleByAuthority(@NonNull String authority) {
        return roleRepository.findByName(authority)
                .map(roleMapper::toDto);
    }
}
