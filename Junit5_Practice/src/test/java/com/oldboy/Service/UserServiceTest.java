package com.oldboy.Service;

import com.oldboy.DAO.UserDao;
import com.oldboy.DTO.CreateUserDto;
import com.oldboy.DTO.UserDto;
import com.oldboy.Entity.Enum.Gender;
import com.oldboy.Entity.Enum.Role;
import com.oldboy.Entity.User;
import com.oldboy.Exception.ValidationException;
import com.oldboy.Mapper.CreateUserMapper;
import com.oldboy.Mapper.UserMapper;
import com.oldboy.Validator.CreateUserValidator;
import com.oldboy.Validator.Error;
import com.oldboy.Validator.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/*
Поскольку мы проводим unit тестирование, а не интеграционные тесты,
нам придется создать mock-заглушки для зависимых классов в UserService:
- CreateUserValidator;
- UserDao;
- CreateUserMapper;
- UserMapper;

На сленге, мы должны "замокать все внешние зависимости", т.к. их тестами
мы будем покрывать отдельно. Подключаем расширения, в данном случае Mockito.
Именно оно будет управлять работой наших объектов помеченных, как @Mock.
*/
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    /*
    Помечаем данной аннотацией все наши mock - заглушки.
    Теперь Mockito будет управлять их работой, расширение
    будет подготавливать его и чистить stub-ы перед каждым
    тестом.
    */
    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    /* Сюда мы будем внедрять mock объекты */
    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccessTest(){
        /* На вход подаем объект User и UserDto */
        User testUser = getUserForTest();
        UserDto testUserDto = getUserDtoForTest();
        /*
        Создаем stub для User - вернуть User-a, когда у UserDao вызывается метод
        *.findByEmailAndPassword() с переданными get параметрами на testUser
        */
        Mockito.doReturn(Optional.of(testUser)).
                when(userDao).
                findByEmailAndPassword(testUser.getEmail(),
                                       testUser.getPassword());
        /*
        Создаем stub для UserDto - вернуть UserDto, когда у UserMapper вызвали метод
        *.map() с переданным в него testUser.
        */
        Mockito.doReturn(testUserDto).when(userMapper).map(testUser);

        Optional<UserDto> actualRes = userService.login(testUser.getEmail(),
                                                        testUser.getPassword());
        Assertions.assertThat(actualRes).isPresent();
        Assertions.assertThat(actualRes.get()).isEqualTo(testUserDto);
    }

    @Test
    void loginFailedTest(){
        /* Создаем stub для заведомо не верифицируемых данных */
        Mockito.doReturn(Optional.empty()).
                when(userDao).
                findByEmailAndPassword(ArgumentMatchers.any(),
                                       ArgumentMatchers.any());
        Optional<UserDto> actualRes = userService.login("some_text","some_number");
        /* Поскольку данные 'не валидные', то и метод вернет пустоту */
        Assertions.assertThat(actualRes).isEmpty();
        /*
        В случае когда метод *.login() принял невалидные данные, преобразование одного объекта
        в другой, т.е. использование UserMapper-a не произойдет, т.е. к нему не будет обращения,
        что и фиксирует данный метод.
        */
        Mockito.verifyNoInteractions(userMapper);
    }

    @Test
    void createMethodTest() {
        /*
        Повторяем по шагам логику метода *.create() из UserService.java
        Сначала формируем то, что должно прийти на вход метода <- Given.
        */
        CreateUserDto createUserDtoForTest = getCreateUserDtoForTest();
        User userForTest = getUserForTest();
        UserDto userDtoForTest = getUserDtoForTest();

        /* Создаем первый stub, для CreateUserValidator */
        Mockito.doReturn(new ValidationResult()).
                       when(createUserValidator).
                  validate(createUserDtoForTest);

        /* Теперь создаем stub для createUserMapper */
        Mockito.doReturn(userForTest).
               when(createUserMapper).
            map(createUserDtoForTest);

        /* Теперь создаем stub для UserMapper */
        Mockito.doReturn(userDtoForTest).
                     when(userMapper).
                     map(userForTest);

        UserDto actualRes = userService.create(createUserDtoForTest);

        Assertions.assertThat(actualRes).isEqualTo(userDtoForTest);
        Mockito.verify(userDao).save(userForTest);
    }

    @Test
    void shouldThrowExceptionIfDtoInvalidInCreateMethodTest(){
        /*
        Создаем CreateUserDto пустышку, хотя мы могли использовать метод
        *.getCreateUserDtoForTest(), для нас это не важно - мы тестируем
        логику метода.
        */
        CreateUserDto createUserDtoForTest = CreateUserDto.builder().build();
        /* Создаем объект аккумулирующий ошибки и помещаем в него одну ошибку */
        ValidationResult validationResultForTest = new ValidationResult();
        validationResultForTest.add(Error.of("invalid.birthday","Birthday is invalid"));

        Mockito.doReturn(validationResultForTest).
                        when(createUserValidator).
                   validate(createUserDtoForTest);
        /* Ожидаем выброс исключения */
        assertThrows(ValidationException.class, () -> userService.create(createUserDtoForTest));
        /*
        Ожидаем отсутствия взаимодействия с userDao, createUserMapper, userMapper,
        т.к. до них после выброса исключения в методе *.create() просто не добрались.
        */
        Mockito.verifyNoInteractions(userDao, createUserMapper, userMapper);
    }

    private static CreateUserDto getCreateUserDtoForTest() {
        return CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("20-11-2004").
                role(Role.USER.name()).
                gender(Gender.MALE.name()).
                build();
    }


    private static User getUserForTest () {
        return User.builder().
                id(77).
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday(LocalDate.of(2012, 02, 04)).
                role(Role.ADMIN).
                gender(Gender.MALE).
                build();
    }
    private static UserDto getUserDtoForTest() {
        return UserDto.builder().
                id(77).
                name("Paul").
                email("paul@arrakis.com").
                birthday(LocalDate.of(2012, 02, 04)).
                role(Role.ADMIN).
                gender(Gender.MALE).
                build();
    }
}