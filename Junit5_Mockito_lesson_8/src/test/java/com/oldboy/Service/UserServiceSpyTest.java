package com.oldboy.Service;
/*
Изучение MOCKITO
Часть 2. Spy - proxy для реальных объектов, которые ведут себя
               точно также, как и объекты тестируемого кода, но
               могут быть запрограммированы как mocks.
*/

import com.oldboy.DAO.UserDao;
import com.oldboy.DTO.User;
import com.oldboy.Extensions.UserServiceParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UserTest")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
/* Задаем порядок прохождения тестов */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({
        UserServiceParamResolver.class
})
public class UserServiceSpyTest {
    private UserService userService;
    private UserDao userDao;
    private static final User PAUL = User.of(1,"Paul","123");
    private static final User VLADIMIR = User.of(2,"Vladimir", "321");
    private static final User RABAN = User.of(3,"Raban","987");

    @BeforeEach
    void prepareBeforeEachTest(){
        /*
        В данном случае мы вместо объекта UserDao создаем прокси-объект.
        */
        this.userDao = Mockito.spy(new UserDao());
        this.userService = new UserService(userDao);
    }

    @Test
    void shouldDeleteExistedUserInDataBase(){
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
        передается MOCK прокси-объект UserDao (см. выше).

        И так, при помощи Mock объекта мы передали ID user-a в метод *.delete() и вот теперь
        assertThat вернет true.
        */
        boolean deleteResult = userService.delete(PAUL.getId());
        /*
        Mockito предоставляет массу методов для контроля над проведением тестов,
        а так же взаимодействия с результатами тестов в процессе. Это механизм
        verify - верификации.

        Например, мы хотим знать сколько раз происходило обращение к UserDao (пусть
        будет 2, хотя обращались в данном методе всего 1 раз). В данном случае
        выбросится исключение, т.к. ожидали 2, а получили только 1.
        */
        ArgumentCaptor<Integer> argumentForCapture = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(userDao, Mockito.times(2)).
                                                    deleteUser(argumentForCapture.capture());
        /*
        Также Mockito предоставляет возможность следить за тестируемыми методами
        и перехватывать значения аргументов - ArgumentCaptor. Приведенная выше
        строка кода перехватывает значение переданное в метод *.deleteUser() класса
        UserDao и теперь эти данные можно проверить на соответствие ID введенного
        user-a.
        */
        assertThat(argumentForCapture.getValue()).isEqualTo(PAUL.getId());
        assertThat(deleteResult).isTrue();
    }

    @Test
    void shouldDeleteExistedUserInDataBaseAnotherVersion(){
        userService.addUser(PAUL);
        /*
        Возможен и другой вариант последовательного вызова методов,
        в отличие от метода: *.shouldDeleteExistedUserInDataBase()
        прописанного выше.

        НО, если, в случае с MOCK (UserServiceMockTest.java), который является
        'заглушкой', данный вызов не ронял тест т.к. реального объекта UserDao
        не участвовал в работе, то теперь тест упадет (нет связи с сервером БД),
        т.к. мы вызываем реальный объект:
        - userDao.deleteUser(PAUL.getId())
        исходя из инициализации его внутри метода prepareBeforeEachTest:
        - this.userDao = Mockito.spy(new UserDao())

        зато первая версия данного метода все еще работает см. выше.
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