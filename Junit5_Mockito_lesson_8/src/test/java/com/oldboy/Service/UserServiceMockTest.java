package com.oldboy.Service;
/*
Изучение MOCKITO
Часть 1. Mock - запрограммированные объекты возвращающие ожидаемый
                результат (stubs) на вызов определенных методов.
                Mock не содержит в себе реальный объект и не
                вызывает его методы.
*/

import com.oldboy.DAO.UserDao;
import com.oldboy.DTO.User;
import com.oldboy.Extensions.UserServiceParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UserTest")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
/* Задаем порядок прохождения тестов */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({
        UserServiceParamResolver.class
})
public class UserServiceMockTest {
    private UserService userService;
    private UserDao userDao;
    private static final User PAUL = User.of(1,"Paul","123");
    private static final User VLADIMIR = User.of(2,"Vladimir", "321");
    private static final User RABAN = User.of(3,"Raban","987");

    @BeforeEach
    void prepareBeforeEachTest(){
        /*
        Мы не можем просто создать объект UserDao нам нужен Mock объект,
        полную копию UserDao или т.н. заглушку. И теперь готовый userDao
        мы можем передать в качестве параметра в UserService.

        Что же делает подставленный в userDao Mockito объект, можно
        посмотреть в примере MyUserDaoMock.java (естественно пример
        упрощенный)
        */
        this.userDao = Mockito.mock(UserDao.class);
        /* Теперь это МОСК объект поставляется в конструктор UserService */
        this.userService = new UserService(userDao);
    }

    @Test
    void shouldDeleteExistedUserInDataBase(){
        /*
        Первоначальный код метода выглядел так:

        // 1. Добавили пользователя в БД (список)
                userService.addUser(PAUL);
        // 2. Запускаем у UserService метод *.delete(), и ожидаем, что получим boolean true
                boolean deleteResult = userService.delete(PAUL.getId());
        // 3. Однако метод по удалению UserService, связан с методом *.deleteUser() класса UserDao
                assertThat(deleteResult).isTrue();

        и естественно, он либо выбрасывал исключение, либо не мог связаться с базой данных.
        */
        userService.addUser(PAUL);
        /*
        Строка приведенная ниже создает т.н. Stub - объект, который используется mocks и
        spies для ответа (Answer) на вызовы методов во время тестов. Если читать строку,
        то получим, что мы просим Mockito вернуть (toBeReturn) - true, (when) когда мы
        вызовем у userDao его метод *.deleteUser() и передадим в него *.getId определенной
        записи в БД (в списке).

        Теперь у нас есть ID пользователя PAUL (id = 1), который можно передать в метод
        *.delete(user Id) объекта UserService, что и делается ниже.
        */
        Mockito.doReturn(true).when(userDao).deleteUser(PAUL.getId());
        /*
        В первом случае мы знаем ID пользователя и извлекаем его для удаления записи из БД.
        Однако, бывает, что нас не интересует ID и мы просто на любой запрос с ID хотим
        вернуть true. Тогда мы можем использовать - Dummy объект - который не используется
        во время тестирования и нужны только для заполнения параметров методов:

        Mockito.doReturn(true).when(userDao).deleteUser(Mockito.any());
        */

        /*
        Наш метод void prepareBeforeEachTest() помеченный как @BeforeEach инициализирует
        все необходимые переменные для всех тестов, в том числе и объект UserService, куда
        передается MOCK объект-заглушку UserDao (см. выше).

        И так, при помощи Mock объекта мы передали ID user-a в метод *.delete() и вот теперь
        assertThat вернет true.
        */
        boolean deleteResult = userService.delete(PAUL.getId());
        assertThat(deleteResult).isTrue();
    }

    @Test
    void shouldDeleteExistedUserInDataBaseAnotherVersion(){
        userService.addUser(PAUL);
        /*
        Возможен и другой вариант последовательного вызова методов,
        в отличие от метода: *.shouldDeleteExistedUserInDataBase()
        прописанного выше.

        В данной версии "массив данных" будет заполняться последовательно
        и первый вызов *.deleteUser(PAUL.getId()) вернет true, а последующие
        false. Выведем результат в консоль.
        */
        Mockito.when(userDao.deleteUser(PAUL.getId())).
                                    thenReturn(true).
                                    thenReturn(false);

        boolean deleteResult = userService.delete(PAUL.getId());
        /*
        В консоли будет:
        true
        false
        false
        */
        System.out.println(deleteResult);
        System.out.println(userService.delete(PAUL.getId()));
        System.out.println(userService.delete(PAUL.getId()));
        /* Поскольку сопоставляем с переменной deleteResult, тест пройдет */
        assertThat(deleteResult).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void loginParametrizedTest(String username){
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        assertThrows(IllegalArgumentException.class,
                     () -> userService.login(username, "no_password"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Paul", "Vladimir", "Raban"
    })
    void loginParametrizedValueSourceTest(String username){
        userService.addMoreOneUsers(PAUL, VLADIMIR, RABAN);
        assertThrows(IllegalArgumentException.class,
                () -> userService.login(username, null));
    }

    @Nested
    @Tag("login")
    class demoOutMethodLikeSourceForThisTest {

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