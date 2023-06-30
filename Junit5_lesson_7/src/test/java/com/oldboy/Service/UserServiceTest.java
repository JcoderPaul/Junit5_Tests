package com.oldboy.Service;
/*
Изучаем DI - Dependency Injection - внедрение зависимостей.
*/

import com.oldboy.DTO.User;
import com.oldboy.Extensions.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/* Например, помечаем тестовый класс целиком */
@Tag("UserTest")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
/* Задаем порядок прохождения тестов */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/*
Для того, что бы запустить наше внедрение зависимости применяется аннотация @ExtendWith, куда
в качестве параметров передается, например, наш класс UserServiceParamResolver.java в котором
уже создан объект UserService и нам его нужно только передать в *.prepareBeforeEachTest(UserService).
В параметры данной аннотации можно передавать массив Resolver - ов и Run - ров, для внедрения
зависимостей и внешних классов управляющих жизненным циклом тестов - расширениям (extensions).
*/
@ExtendWith({
        UserServiceParamResolver.class,
        /* Добавляем наше расширение в тесты */
        GlobalExtension.class,
        PostProcessionExtension.class,
        ConditionalExtension.class,
        ThrowableExtension.class
})
public class UserServiceTest {
    /* Создаем поле общее для всех тестов*/
    private UserService userService;
    /* Поля необходимые для демонстрации TDD концепции */
    private static final User PAUL = User.of(1,"Paul","123");
    private static final User VLADIMIR = User.of(2,"Vladimir", "321");
    private static final User RABAN = User.of(3,"Raban","987");

    /*
    В предыдущих версиях Junit не было возможности
    создавать конструкторы тестового класса, сейчас
    такая возможность есть. Создадим конструктор без
    реализации логики внутри и передадим в него некие
    параметры.

    В данном случае наш тестовый класс зависит от
    TestInfo
    */
    UserServiceTest (TestInfo testInfo) {
        System.out.println(testInfo);
    }
    /*
    Из внешнего класса UserServiceParamResolver получаем объект UserService.
    В данном случае мы не сами создали (не сам метод реализовал свою зависимость),
    а внедрили ее извне используя внешний класс, который нам ее предоставил по
    средствам аннотации @ExtendWith над всем тестовым классом и реализации методов
    внутри UserServiceParamResolver
    */
    @BeforeEach
    void prepareBeforeEachTest(UserService userService){
        /*
        См. более ранние варианты данного метода lesson_6, где в
        параметрах данного метода ничего не передавалось, а сам
        объект UserService создавался прямо тут.
        */
        this.userService = userService;
    }

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
    @SneakyThrows
    void loginParametrizedValueSourceTest(String username){
        /*
        !!! Преднамеренно введем проброс исключения !!!
        Проверим работу класса ThrowableExtension.java
        */
        if (true){
            throw new IOException();
            /*
            Тест в данном случае упадет, но если
            мы заменим предыдущую строку на:

            throw new RuntimeException();

            тест пройдет т.к. не соответствует условиям
            в методе *.handleTestExecutionException()
            класса ThrowableExtension.java
            */
        }

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
        Аннотация @MethodSource("com.oldboy.Servise.UserServiceTest#getArgsForParamLoginTest")
        включает путь к поставщику ресурсов для теста:
        - com.oldboy.Service.UserServiceTest - класс, где лежит метод поставщик данных;
        - getArgsForParamLoginTest - сам метод, который поставляет данные для теста.
        */
        @ParameterizedTest
        @MethodSource("com.oldboy.Service.UserServiceTest#getArgsForParamLoginTest")
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
    @CsvSource({
           "Paul,123",
           "Vladimir,321",
           "Raban,987",
    })
    void demoCsvGetParameterForTest(String name, String pass) {
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        Optional<User> maybeUser = userService.login(name, pass);
        assertThat(maybeUser.get().getUsername()).isEqualTo(name);
        assertThat(maybeUser.get().getPassword()).isEqualTo(pass);
    }
}