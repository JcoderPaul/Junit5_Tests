package com.oldboy;
/*
Добавим методы аннотированные @BeforeEach и @AfterEach,
которые будут запускаться перед каждым и после каждого
стандартного метода аннотированного @Test.

!!! Нужно обратить внимание, что для запуска каждого
теста создается свой объект ThirdTest (this)!!!
*/
import com.oldboy.DTO.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* Так же модификатор доступа public в Junit 5 опускается */
public class ThirdTest {
    /* Создаем поле общее для всех тестов*/
    private UserService userService;
    /*
    Тест, который на практике проводит некие подготовительные манипуляции
    для тестирования всех остальных тестов и выполняется перед ними. Данный
    метод будет запускаться перед каждым стандартным тестом.
    */
    @BeforeEach
    void prepareBeforeEachTest(){
        /* Инициализируем переменную */
        userService = new UserService();
        System.out.println("Before each : " + this);
    }
    /*
    Junit 5 не требует чтобы тестовый метод имел постфикс или суффикс Test,
    как это было в Junit 4, тут достаточно указать аннотацию @Test
    */
    @Test
    void noUserInDataBaseTest(){
        System.out.println("noUserInDataBaseTest : " + this);

        List<User> getUser = userService.getAll();
        assertTrue(getUser.isEmpty());
    }

    @Test
    void getEmptySizeBaseAfterAddTwoUserTest(){
        System.out.println("getEmptySizeBaseAfterAddTwoUserTest : " + this);

        userService.addUser(new User());
        userService.addUser(new User());

        List<User> testBase = userService.getAll();
        assertEquals(2,testBase.size());
    }
    /*
    Обычно, данной аннотацией помечают метод выполняемый
    после всех других тестов, и например, проводит очистку
    неких данных и т.п. (приводит тестируемый объект в
    первоначальное состояние) Данный метод будет запускаться
    после каждого теста.
    */
    @AfterEach
    void afterDeleteAllUserFromDatabase(){
        System.out.println("deleteAllUserFromDatabase : " + this);
    }
}
