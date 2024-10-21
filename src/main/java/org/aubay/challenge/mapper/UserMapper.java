package org.aubay.challenge.mapper;

import org.aubay.challenge.dto.UserDto;
import org.aubay.challenge.entity.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(Users user);
    List<UserDto> userToUserDto(List<Users> user);

    Users userDtoToUsers(UserDto userDto);
    List<Users> userDtoToUsers(List<UserDto> userDto);

}
