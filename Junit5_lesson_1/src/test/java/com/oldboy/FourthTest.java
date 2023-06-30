package com.oldboy;
/*
Добавим методы аннотированные @BeforeAll и @AfterAll,
которые будут запускаться по одному разу каждый -
перед всеми тестами и после всех тестов помеченных
аннотацией @Test и методах @BeforeEach и @AfterEach.

!!! Нужно обратить внимание, что для запуска каждого
теста создается свой объект ThirdTest (this)!!!
*/
import com.oldboy.DTO.User;
import com.oldboy.UserService;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
Так же модификатор доступа public в Junit 5 опускается
Чтобы была возможность запустить методы аннотированные
@BeforeAll и @AfterAll они должны быть статическими, а
основной тестовый класс аннотирован @TestInstance. При
такой настройке для каждого теста будет создаваться
новый объект FourthTest, при выборе параметра:
TestInstance.Lifecycle.PER_METHOD.

В случае же, когда мы хотим чтобы во всех тестах
участвовал один объект, то параметр Instance аннотации
должен быть: TestInstance.Lifecycle.PER_CLASS и
в таком случае методы @BeforeAll и @AfterAll не обязаны
быть статическими.
*/
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FourthTest {
    /* Создаем поле общее для всех тестов*/
    private UserService userService;

    @BeforeAll
    static void initForAllTests(){
        System.out.println("initForAllTests");
    }
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
    /* Данный метод должен быть статическим */
    @AfterAll
    static void afterAllTests(){
        System.out.println("afterAllTests");
    }
}
