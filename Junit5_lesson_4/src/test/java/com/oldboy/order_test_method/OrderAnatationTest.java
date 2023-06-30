package com.oldboy.order_test_method;
/*
Исследуем - @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
*/

import com.oldboy.DTO.User;
import com.oldboy.UserService;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/* Например, помечаем тестовый класс целиком */
@Tag("UserTest")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
/* Задаем порядок прохождения тестов */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderAnatationTest {
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
    @Order(1)
    void getNotEmptySizeBaseAfterAddTwoUserTest(){
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
    @Order(3)
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

    /* Снова используем TDD */
    @Test
    @Tag("convertListToMapMethod")
    @Order(2)
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
}
