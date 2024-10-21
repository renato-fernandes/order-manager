package org.aubay.challenge.service;

import org.aubay.challenge.dto.UserDto;
import org.aubay.challenge.entity.Users;
import org.aubay.challenge.mapper.UserMapper;
import org.aubay.challenge.repository.UsersRepository;
import org.aubay.challenge.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Autowired
    public UsersService(UsersRepository usersRepository, UserMapper userMapper){
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {
        return saveUser(userDto);
    }

    private UserDto saveUser(UserDto userDto){
        userDto.setEmail(userDto.getEmail().toLowerCase());
        return userMapper.userToUserDto(
                usersRepository.saveAndFlush(
                        userMapper.userDtoToUsers(userDto)
                )
        );
    }

    public List<UserDto> getAllUsers() {
        return userMapper.userToUserDto((usersRepository.findAll()));
    }

    public List<UserDto> getUser(String email) {
        return userMapper.userToUserDto(usersRepository.findByEmail(email));
    }

    private Optional<Users> getUser(Long id){
        return usersRepository.findById(id);
    }

    public UserDto getUserDto(Long id){
        Optional<Users> response = getUser(id);
        return userMapper.userToUserDto(response.orElse(null));
    }

    public UserDto updateUser(UserDto userDto) {

        if(!getUser(userDto.getId()).isPresent()){
            throw new IllegalArgumentException(String.format(Constants.INVALID_ID_ERROR, Constants.USER, userDto.getId()));
        }
        return saveUser(userDto);

    }

    public void deleteUser(UserDto userDto) {
        usersRepository.delete(userMapper.userDtoToUsers(userDto));
    }
}
