package parcel.delivery.app.auth.service;

import parcel.delivery.app.auth.dto.UserDto;

import java.util.Optional;

public interface UserService {
    void save(UserDto user);

    Optional<UserDto> findByClientId(String username);
}
