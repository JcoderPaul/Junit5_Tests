package com.oldboy;
/*
В данном примере мы тихонечко применяем TDD - test-driven development
или разработка через тестирование. Пока у нас нет базы данных, и в ней
нет записей о пользователях, и это мы собираемся проверить. Мы создаем
класс UserService, в котором должен быть метод *.getAll() - возвращающий
полный список List наших пользователей в базе данных и поскольку список
пуст, метод *.isEmpty() должен вернуть true, т.е. тест должен пройти.
*/
import com.oldboy.DTO.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* Так же модификатор доступа public в Junit 5 опускается */
public class SecondTest {

    /*
    Junit 5 не требует чтобы тестовый метод имел постфикс или суффикс Test,
    как это было в Junit 4, тут достаточно указать аннотацию @Test
    */
    @Test
    void noUserInDataBaseTest(){
        System.out.println("noUserInDataBaseTest : " + this);
        UserService userService = new UserService();
        List<User> getUser = userService.getAll();
        assertTrue(getUser.isEmpty());
    }

    @Test
    void getEmptySizeBaseAfterAddTwoUserTest(){
        System.out.println("getEmptySizeBaseAfterAddTwoUserTest : " + this);
        UserService userService = new UserService();
        userService.addUser(new User());
        userService.addUser(new User());

        List<User> testBase = userService.getAll();
        assertEquals(2,testBase.size());
    }
}
