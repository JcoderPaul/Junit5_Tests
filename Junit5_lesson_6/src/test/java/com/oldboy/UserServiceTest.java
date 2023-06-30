package com.oldboy;
/*
Flaky-тест, буквально “хлопчатый”, “рассыпающийся на кусочки”,
в индустрии ИТ-тестирования означает нестабильный, ненадежный
тест, который иногда “pass”, иногда “fail”, и трудно понять,
по какой закономерности.

Методик решения данной проблемы много, тут мы рассмотрим два
метода, но они ни сколько решают проблему 'мерцающих' тестов
сколько позволяют исключить их на время из процесса
тестирования.

Аннотации @Disable, @RepeatedTest(value, name), @TimeOut() и
assertTimeout
*/

import com.oldboy.DTO.User;
import com.oldboy.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;


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
    private static final User FEID = User.of(3,"Feid","257");

    @BeforeEach
    void prepareBeforeEachTest(){
        /* Инициализируем переменную */
        userService = new UserService();
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN, FEID);
    }

    /*
    Отключим тест, как нестабильный и оставим некие комментарии,
    чтобы вернуться к нему позже и разобраться в его 'мерцании'
    */
    @Disabled("Flaky, need to see later")
    @ParameterizedTest
    @NullAndEmptySource
    void loginParametrizedTest(String username){
        /* Вся загрузка пользователей в БД (список) в методе @BeforeEach*/
        assertThrows(IllegalArgumentException.class,
                     () -> userService.login(username, "no_password"));
    }

    /*
    Не знаю каким образом может помочь многократный
    запуск тестов, нож однако у нас есть аннотация
    позволяющая многократно гонять тест по кругу в
    ожидании возможного fail.

    Кроме самой аннотации @RepeatedTest мы можем
    задать количество повторов и как-то облагородить
    выводимое название теста.

    Так же, мы можем в качестве параметров передать
    объект repetitionInfo, и при пошаговом изучении
    кода использовать эти данные в своих интересах.
    */
    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void loginParametrizedValueSourceTest(RepetitionInfo repetitionInfo){
        /* Вся загрузка пользователей в БД (список) в методе @BeforeEach*/
        assertThrows(IllegalArgumentException.class,
                () -> userService.login(PAUL.getUsername(), null));
    }

    @Nested
    @Tag("login")
    class demoOutMethodLikeSourceForThisTest {

        @ParameterizedTest
        @MethodSource("com.oldboy.UserServiceTest#getArgsForParamLoginTest")
        void loginParametrizedWithMethodSourceTest(String name,
                                                   String pass,
                                                   Optional<User> user){
            /* Зададим некий таймаут, который тест не должен превышать */
            assertTimeout(Duration.ofMillis(200), () -> {
                /*
                Умышленно уроним тест задав остановку
                потока больше чем критический таймаут
                теста.
                */
                Thread.sleep(300);
                Optional<User> maybeUser = userService.login(name, pass);
                assertThat(maybeUser).isEqualTo(user);
            });

        }
    }

    static Stream<Arguments> getArgsForParamLoginTest(){
        return Stream.of(
                Arguments.of("Paul", "123", Optional.of(PAUL)),
                Arguments.of("Vladimir", "321", Optional.of(VLADIMIR)),
                Arguments.of("Raban","987", Optional.of(RABAN)),
                Arguments.of("Paul", "not_right_pass", Optional.empty()),
                Arguments.of("not_right_name","123", Optional.empty())
        );
    }

    /*
    Еще один способ задать таймаут на выполнение теста или набора тестов
    т.к. данную аннотацию можно разместить над всем тестовым классом.
    */
    @Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
    @ParameterizedTest(name = "{arguments} test {index}")
    @CsvFileSource(resources = "/login-pass-data-for-test.csv", // адрес файла
                   delimiter = ',', // разделитель
                   numLinesToSkip = 1) // отступ от начала файла
    void demoCsvGetParameterForTest(String name, String pass) {
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        Optional<User> maybeUser = userService.login(name, pass);
        assertThat(maybeUser.get().getUsername()).isEqualTo(name);
        assertThat(maybeUser.get().getPassword()).isEqualTo(pass);
    }
}