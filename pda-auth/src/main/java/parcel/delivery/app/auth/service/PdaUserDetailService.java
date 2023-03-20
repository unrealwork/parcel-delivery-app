package parcel.delivery.app.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.domain.Privilege;
import parcel.delivery.app.auth.domain.Role;
import parcel.delivery.app.auth.domain.User;
import parcel.delivery.app.auth.repository.UserRepository;
import parcel.delivery.app.auth.security.auth.PdaUserDetails;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.common.security.core.UserRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PdaUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDto = userRepository.findByClientIdEqualsIgnoreCase(username);
        return userDto.map(PdaUserDetailService::fromUserDomain)
                .orElseThrow(() -> new UsernameNotFoundException("User with clientId " + username + " is not found"));
    }

    private static UserDetails fromUserDomain(User user) {
        Collection<? extends GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(PdaUserDetailService::roleToAuthorities)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        return PdaUserDetails.create(user.getClientId(), user.getPassword(), authorities);
    }

    private static Collection<? extends GrantedAuthority> roleToAuthorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>(1 + role.getPrivileges()
                .size());
        UserRole userRole = UserRole.fromAuthority(role.getName());
        authorities.add(userRole);
        for (Privilege privilege : role.getPrivileges()) {
            authorities.add(RolePrivilege.fromAuthority(privilege.getName()));
        }
        return authorities;
    }
}
