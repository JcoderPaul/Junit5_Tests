package com.oldboy;
/*
Изучение параметризированные тесты @ParameterizedTest.
*/

import com.oldboy.DTO.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/* Например, помечаем тестовый класс целиком */
@Tag("UserTest")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
/* Задаем порядок прохождения тестов */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    /*
    Включите зависимость junit-jupiter-params в pom.xml, чтобы использовать
    параметризованные тесты. В данном случае аннотации следующие после
    основной предоставляют ресурсы для тестирования, но только один параметр,
    например 'username' в текущем примере.


    Если запустить данный тест в дебаге, то можно увидеть, как соответствующие
    параметры пришли в тестовый метод сначала null затем empty или "", согласно
    аннотациям.
    */
    @ParameterizedTest
    /* Предоставляет null параметр */
    @NullSource
    /* Предоставляет пустой параметр или "" */
    @EmptySource
    /* Есть аннотация, которая объединяет две первые: @NullAndEmptySource */
    void loginParametrizedTest(String username){
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        assertThrows(IllegalArgumentException.class,
                     () -> userService.login(username, "no_password"));
    }

    /*
    Поставщиком данных в текущем случае будет
    @ValueSource, так же в цикле, как и в примере
    выше
    */
    @ParameterizedTest
    @ValueSource(strings = {
            "Paul", "Vladimir", "Raban"
    })
    void loginParametrizedValueSourceTest(String username){
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        assertThrows(IllegalArgumentException.class,
                () -> userService.login(username, null));
    }

    /*
    Чтобы продемонстрировать как прописывать (имя)путь к ресурсному методу
    применим вложенный тестовый класс. Метод внутри данного вложенного класса
    не сможет увидеть статический метод, который будет поставлять данные для
    него, находящийся во внешнем классе.
    */
    @Nested
    @Tag("login")
    class demoOutMethodLikeSourceForThisTest {

        /*
        Аннотация @MethodSource("com.oldboy.UserServiceTest#getArgsForParamLoginTest")
        включает путь к поставщику ресурсов для теста:
        - com.oldboy.UserServiceTest - класс, где лежит метод поставщик данных;
        - getArgsForParamLoginTest - сам метод, который поставляет данные для теста.
        */
        @ParameterizedTest
        @MethodSource("com.oldboy.UserServiceTest#getArgsForParamLoginTest")
        void loginParametrizedWithMethodSourceTest(String name,
                                                   String pass,
                                                   Optional<User> user){
            userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
            Optional<User> maybeUser = userService.login(name, pass);
            assertThat(maybeUser).isEqualTo(user);
        }
    }
    /*
    Данный метод *.getArgsForParamLoginTest() является поставщиком ресурсов
    для другого метода *.loginParametrizedWithMethodSourceTest(name, pass, user)
    помеченного аннотацией @MethodSource, в параметрах аннотации которого указан
    путь к данному методу.

    Данный метод возвращает stream аргументов для тестового метода, который
    принимает их и перебирает тестируя исследуемый метод *.login(name, pass).
    */
    static Stream<Arguments> getArgsForParamLoginTest(){
        return Stream.of(
                Arguments.of("Paul", "123", Optional.of(PAUL)),
                Arguments.of("Vladimir", "321", Optional.of(VLADIMIR)),
                Arguments.of("Raban","987", Optional.of(RABAN)),
                Arguments.of("Paul", "not_right_pass", Optional.empty()),
                Arguments.of("not_right_name","123", Optional.empty())
        );
    }

    @ParameterizedTest(name = "{arguments} test {index}")
    /*
    В данной аннотации указывается путь к файлу ресурсов,
    разделитель (у нас это запятая) и количество строк
    отступа от верхней границы файла, т.к. там у нас
    заголовок (USERNAME,PASSWORD).

    Ограничения такого способа поставки ресурсов для тестов
    это невозможность поставить сложный ресурс - объект, как
    это было в случае с @MethodSource.
    */
    @CsvFileSource(resources = "/login-pass-data-for-test.csv", // адрес файла
                   delimiter = ',', // разделитель
                   numLinesToSkip = 1) // отступ от начала файла
    /*
    Аналогом @CsvFileSource является аннотация:
    @CsvSource({
           "Paul,123",
           "Vladimir,321",
           "Raban,987",
           "Feid,257"

    При этом параметры могут быть разными например Integer,
    см. описания самих аннотаций и документацию по Junit 5.
    })
    */
    void demoCsvGetParameterForTest(String name, String pass) {
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        Optional<User> maybeUser = userService.login(name, pass);
        assertThat(maybeUser.get().getUsername()).isEqualTo(name);
        assertThat(maybeUser.get().getPassword()).isEqualTo(pass);
    }
}