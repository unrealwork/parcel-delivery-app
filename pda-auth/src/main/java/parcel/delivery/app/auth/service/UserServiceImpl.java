package parcel.delivery.app.auth.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.domain.User;
import parcel.delivery.app.auth.dto.UserDto;
import parcel.delivery.app.auth.mapper.UserMapper;
import parcel.delivery.app.auth.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public void save(@NotBlank UserDto user) {
        User userEntity = userMapper.toEntity(user);
        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> findByClientId(String username) {
        return userRepository.findByClientIdEqualsIgnoreCase(username)
                .map(userMapper::toDto);
    }
}
