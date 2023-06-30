package com.oldboy;
/*
Примеры работы с библиотеками AssertJ (очень кратко)
см. DOC/AssertJ.txt. Примеры:
- getEmptySizeBaseAfterAddTwoUserTest()
- loginSuccessIfUserExists()
- equalsUserFromLoginForm
- convertedUserListToMapById()

Как использовать исключения в тестировании, примеры:
- throwExceptionIfUsernameIsNull()
- throwExceptionInAllSituationOnLoginNullableEnter()
*/
import com.oldboy.DTO.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
/* Например, помечаем тестовый класс целиком */
@Tag("UserTest")
public class UserServiceTest {
    /* Создаем поле общее для всех тестов*/
    private UserService userService;
    /* Поля необходимые для демонстрации TDD концепции */
    private static final User PAUL = User.of(1,"Paul","123");
    private static final User VLADIMIR = User.of(2,"Vladimir", "321");
    private static final User RABAN = User.of(3,"Raban","987");

    @BeforeEach
    void prepareBeforeEachTest(){
        /* Инициализируем переменную */
        userService = new UserService();
    }

    @Test
    @Tag("addUserMethod")
    void getEmptySizeBaseAfterAddTwoUserTest(){
        userService.addUser(RABAN);

        List<User> testBase = userService.getAll();

        /*
        Заменим метод *.assertEquals(1,testBase.size()) из библиотеки
        org.junit.jupiter на метод из библиотеки org.assertj.core.api
        - *.assertThat(). Если в первом случае мы передавали в метод
        два параметра "эталонное значение" и фактическое тестируемое,
        то в данном методе уже передаем только один параметр - название
        базы, и уже теперь просто замеряем размер ее сопоставляя с эталоном.

        При этом нам доступны и другие методы для проверки.
        */
        assertThat(testBase).hasSize(1);
        assertThat(testBase).isNotEmpty();

    }

    @Test
    @Tag("login")
    void loginSuccessIfUserExists(){
        /* Добавляем пользователя в пустой список (имитация добавления в БД) */
        userService.addUser(PAUL);
        /* Получаем только что введенного пользователя из тестируемого метода */
        Optional<User> loginUser = userService.login(PAUL.getUsername(),
                                                     PAUL.getPassword());
        /*
        При использовании библиотеки AssertJ код чуть измениться на повествовательный:
        - было assertTrue(loginUser.isPresent());
        - стало см. ниже
        */
        assertThat(loginUser).isPresent();

    }

    @Test
    @Tag("login")
    void equalsUserFromLoginForm(){
        userService.addUser(VLADIMIR);
        Optional<User> maybeUserFromBase = userService.login(VLADIMIR.getUsername(),
                                                             VLADIMIR.getPassword());
        /*
        При использовании библиотеки AssertJ код чуть измениться на повествовательный:
        - было
        ******************************************************************************
        maybeUserFromBase.ifPresent(new Consumer<User>() {
            @Override
            public void accept(User userFromBase) {
                assertEquals(VLADIMIR, userFromBase);
            }
        });
        ******************************************************************************
        - стало см. ниже
        */
        maybeUserFromBase.ifPresent(user -> assertThat(user).isEqualTo(VLADIMIR));
    }
    /* Снова используем TDD */
    @Test
    @Tag("convertListToMapMethod")
    void convertedUserListToMapById(){
        /* Применяем метод которого нет - списочная загрузка пользователей в базу */
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        /*
        Конвертируем список пользователей в MAP методом которого еще нет в UserService.
        Предполагается, что в качестве ключа MAP будет ID объекта user, а value сам объект.
        */
        Map<Integer, User> usersMap = userService.getAllUsersAndConvertToMapById();
        /*
        Применяем пакетный запуск ассертов, который позволяет проверить все
        применяемые в данном тесте ассерты на итоговый результат, несмотря
        на то, что один или несколько из них могут выкинуть исключение и т.п.
        */
        assertAll(
                () -> assertThat(usersMap).containsKeys(PAUL.getId(), VLADIMIR.getId(), RABAN.getId()),
                () -> assertThat(usersMap).containsValues(PAUL, VLADIMIR, RABAN)
        );
    }

    /*
    Тест обрабатывающий исключение т.е. если наш
    тестируемый метод выбросит исключение, значит
    тест пройдет успешно.
    */
    @Test
    @Tag("login")
    void throwExceptionIfUsernameIsNull(){
        try {
            userService.login(null, "not_number");
            Assertions.fail("Login method throw exception on null username");
        } catch (IllegalArgumentException ex){
            assertTrue(true);
        }
        /* Более короткий код выглядит так:

        assertThrows(IllegalArgumentException.class,
                     () -> userService.login(null, "not_number"))

        */
    }

    /*
    Если же нам хочется проверить все возможные сочетания null,
    т.е. либо только логин был null, или только пароль, или и
    то и другое сразу.
    */
    @Test
    @Tag("login")
    void throwExceptionInAllSituationOnLoginNullableEnter(){
       assertAll(
               ()-> assertThrows(IllegalArgumentException.class,
                       () -> userService.login(null, "not_number")),
               ()-> assertThrows(IllegalArgumentException.class,
                       () -> userService.login("PAUL", null)),
               ()-> assertThrows(IllegalArgumentException.class,
                       () -> userService.login(null, null))
       );

    }

    @Test
    @Tag("login")
    void loginFailIfPasswordNotCorrect(){
        /* Помещаем пользователя в БД (список)*/
        userService.addUser(PAUL);
        /* Пытаемся извлечь данного пользователя, но указываем пароль заведомо неверно */
        Optional<User> notUserInBase = userService.login(PAUL.getUsername(), "456");
        /*
        Основная задача, проверить работоспособность метода *.login(name, pass)
        на корректность взаимосвязи логина и пароля при их одновременном вводе.
        */
        assertTrue(notUserInBase.isEmpty());
    }

    @Test
    @Tag("login")
    void loginFailIfUserNotExist(){

        Optional<User> notUserInBase = userService.login("Raban", VLADIMIR.getPassword());

        assertTrue(notUserInBase.isEmpty());
    }
}
