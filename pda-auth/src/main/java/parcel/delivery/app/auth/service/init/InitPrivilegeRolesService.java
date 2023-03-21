package parcel.delivery.app.auth.service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.repository.PrivilegeRepository;
import parcel.delivery.app.auth.repository.RoleRepository;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
final class InitPrivilegeRolesService implements Initializer {
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;


    private final Map<RolePrivilege, Privilege> privilegeDict = new EnumMap<>(RolePrivilege.class);
    private final Map<UserRole, Role> roleDict = new EnumMap<>(UserRole.class);

    @Override
    public void init() {
        log.info("Loading default roles");
        loadDefaultRoles();
    }

    private void loadDefaultRoles() {
        for (RolePrivilege rolePrivilege : RolePrivilege.values()) {
            privilegeDict.computeIfAbsent(rolePrivilege, this::loadPrivilege);
        }

        for (UserRole userRole : UserRole.values()) {
            roleDict.computeIfAbsent(userRole, this::loadUserRole);
        }
    }

    private Role loadUserRole(UserRole userRole) {
        return roleRepository.findByName(userRole.getAuthority())
                .orElseGet(() -> this.saveRole(userRole));
    }

    private Role saveRole(UserRole userRole) {
        Role role = Role.builder()
                .privileges(this.loadPrivileges(userRole))
                .name(userRole.getAuthority())
                .build();
        return roleRepository.save(role);
    }

    private Collection<Privilege> loadPrivileges(UserRole userRole) {
        return userRole.privileges()
                .stream()
                .map(privilegeDict::get)
                .collect(Collectors.toSet());
    }

    private Privilege loadPrivilege(RolePrivilege rolePrivilege) {
        return privilegeRepository.findByName(rolePrivilege.getAuthority())
                .orElseGet(() -> this.savePrivilege(rolePrivilege));
    }

    private Privilege savePrivilege(RolePrivilege rolePrivilege) {
        Privilege privilege = Privilege.builder()
                .name(rolePrivilege.name())
                .build();
        return privilegeRepository.save(privilege);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
