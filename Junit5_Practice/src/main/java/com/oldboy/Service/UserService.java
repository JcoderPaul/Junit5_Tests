package com.oldboy.Service;

import com.oldboy.DAO.UserDao;
import com.oldboy.DTO.CreateUserDto;
import com.oldboy.DTO.UserDto;
import com.oldboy.Entity.User;
import com.oldboy.Exception.ValidationException;
import com.oldboy.Mapper.CreateUserMapper;
import com.oldboy.Mapper.UserMapper;
import com.oldboy.Validator.CreateUserValidator;
import com.oldboy.Validator.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final CreateUserValidator createUserValidator;
    private final UserDao userDao;
    private final CreateUserMapper createUserMapper;
    private final UserMapper userMapper;

    public Optional<UserDto> login(String email, String password) {
        /*
        Развернутый код:
        Optional<User> ifUserHere = userDao.findByEmailAndPassword(email, password);
        Optional<UserDto> isUserDtoHere = ifUserHere.map(object -> userMapper.map(object));
        return isUserDtoHere;
        */
        return userDao.findByEmailAndPassword(email, password).
                       map(user -> userMapper.map(user));
    }

    @SneakyThrows
    public UserDto create(CreateUserDto userDto) {
        /*
        Следующие блоки нам придется тестировать и создавать на них stub-ы:
        - использование ValidationResult
        */
        ValidationResult validationResult = createUserValidator.validate(userDto);
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getErrors());
        }
        /* - использование createUserMapper */
        User userEntity = createUserMapper.map(userDto);
        /*
        Не забываем, что метод *.save() возвращает тот же userEntity,
        переданный ему как аргумент, но с добавленным id, полученным
        из БД
        */
        userDao.save(userEntity);
        /* - использование UserMapper-а, который и превратит User в UserDto */
        return userMapper.map(userEntity);
    }
}
