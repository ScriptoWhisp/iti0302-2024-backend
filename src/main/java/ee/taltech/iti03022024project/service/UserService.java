package ee.taltech.iti03022024project.service;


import ee.taltech.iti03022024project.controller.UserDto;
import ee.taltech.iti03022024project.mapstruct.UserMapper;
import ee.taltech.iti03022024project.repository.UserEntity;
import ee.taltech.iti03022024project.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    public List<UserDto> getUsers() {
        return usersRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    public Optional<UserDto> getUserById(int id) {
        return usersRepository.findById(id).map(userMapper::toDto);
    }

    public Optional<UserDto> createUser(UserDto userDto) {
        UserEntity newUser = userMapper.toEntity(userDto);
        usersRepository.save(newUser);
        return Optional.of(userMapper.toDto(newUser));
    }

    public Optional<UserDto> updateUser(int id, UserDto userDto) {
        Optional<UserEntity> userToUpdate = usersRepository.findById(id);
        userToUpdate.ifPresent(user -> {
            user.setFirst_name(userDto.getFirstName() != null ? userDto.getFirstName() : user.getFirst_name());
            user.setLast_name(userDto.getLastName() != null ? userDto.getLastName() : user.getLast_name());
            user.setLocation(userDto.getLocation() != null ? userDto.getLocation() : user.getLocation());
            usersRepository.save(user);
        });
        return userToUpdate.map(userMapper::toDto);
    }

    public Optional<UserDto> deleteUser(int id) {
        Optional<UserEntity> userToDelete = usersRepository.findById(id);
        userToDelete.ifPresent(usersRepository::delete);
        return userToDelete.map(userMapper::toDto);
    }

}
