package parcel.delivery.app.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parcel.delivery.app.auth.annotations.mapper.PasswordEncodingMapping;
import parcel.delivery.app.auth.domain.User;
import parcel.delivery.app.auth.dto.UserDto;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper extends EntityDtoMapper<User, UserDto> {
    @Override
    @Mapping(target = "password", qualifiedBy = PasswordEncodingMapping.class)
    User toEntity(UserDto entity);
}
