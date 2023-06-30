package com.oldboy.alltests;
/*
Пример применения методологии TDD на примере тестовых методов:
- loginSuccessIfUserExists()
- equalsUserFromLoginForm()
- loginFailIfPasswordNotCorrect()
- loginFailIfUserNotExist()
см. ниже.
*/
import com.oldboy.DTO.User;
import com.oldboy.UserService;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TddMethodLess {
    /* Создаем поле общее для всех тестов*/
    private UserService userService;
    /* Поля необходимые для демонстрации TDD концепции */
    private static final User PAUL = User.of(1,"Paul","123");
    private static final User VLADIMIR = User.of(2,"Vladimir", "321");

    @BeforeEach
    void prepareBeforeEachTest(){
        /* Инициализируем переменную */
        userService = new UserService();
        System.out.println("Before each : " + this);
    }

    @Test
    void loginSuccessIfUserExists(){
        /* Добавляем пользователя в пустой список (имитация добавления в БД) */
        userService.addUser(PAUL);
        /* Получаем только что введенного пользователя из тестируемого метода */
        Optional<User> loginUser = userService.login(PAUL.getUsername(), PAUL.getPassword());
        /*
        Поскольку пользователь в базе (в списке) может быть,
        а может и не быть, он у нас класса Optional<User> и
        теперь нам надо его проверить на наличие после запуска
        тестируемого метода.
        */
        assertTrue(loginUser.isPresent());
    }

    @Test
    void equalsUserFromLoginForm(){
        userService.addUser(VLADIMIR);

        Optional<User> maybeUserFromBase = userService.login(VLADIMIR.getUsername(), VLADIMIR.getPassword());

        /*
        Метод *.ifPresent() позволяет выполнить какое-то действие, если объект не пустой.
        Если в нашем случае ошибок нет, то объект полученный методом *.login(name, pass)
        поступает для сравнения в метод *.assertEquals() с первоначально введенным.
        */
        maybeUserFromBase.ifPresent(new Consumer<User>() {
            @Override
            public void accept(User userFromBase) {
                assertEquals(VLADIMIR, userFromBase);
            }
        });
    }
    @Test
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
    void loginFailIfUserNotExist(){

        Optional<User> notUserInBase = userService.login("Raban", VLADIMIR.getPassword());

        assertTrue(notUserInBase.isEmpty());
    }
}
