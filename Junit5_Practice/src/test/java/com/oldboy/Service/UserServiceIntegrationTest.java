package com.oldboy.Service;

import com.oldboy.DAO.UserDao;
import com.oldboy.DTO.CreateUserDto;
import com.oldboy.DTO.UserDto;
import com.oldboy.Entity.Enum.Gender;
import com.oldboy.Entity.Enum.Role;
import com.oldboy.Entity.User;
import com.oldboy.Mapper.CreateUserMapper;
import com.oldboy.Mapper.UserMapper;
import com.oldboy.Validator.CreateUserValidator;
import com.oldboy.integration.IntegrationTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceIntegrationTest extends IntegrationTestBase {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void init(){
        userDao = UserDao.getInstance();
        userService = new UserService(
                CreateUserValidator.getInstance(),
                userDao,
                CreateUserMapper.getInstance(),
                UserMapper.getInstance()
        );
    }

    @Test
    void loginTest(){
        User userForTest = userDao.save(getUserForTest("test@yandex.ru"));

        Optional<UserDto> expectedRes = userService.login(userForTest.getEmail(),
                                                      userForTest.getPassword());
        Assertions.assertThat(expectedRes).isPresent();
        Assertions.assertThat(expectedRes.get().getId()).isEqualTo(userForTest.getId());
    }

    @Test
    void createTest(){
        CreateUserDto createUserDto = getUserDtoForTest();
        UserDto expectedRes = userService.create(createUserDto);
        assertNotNull(expectedRes.getId());
    }

    private static CreateUserDto getUserDtoForTest() {
        return CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("2012-02-04").
                role(Role.USER.name()).
                gender(Gender.MALE.name()).
                build();
    }
    private static User getUserForTest (String email) {
        return User.builder().
                name("Paul").
                email(email).
                password("342").
                birthday(LocalDate.of(2012, 02, 04)).
                role(Role.ADMIN).
                gender(Gender.MALE).
                build();
    }
}