package com.oldboy.DAO;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class UserDao {

    @SneakyThrows
    public boolean deleteUser(Integer userId) {
        /*
        Базы у нас нет, имитируем подключение к ней, что автоматически выбросить
        исключение, в тестовом классе UserServiceTest при текущих настройках всех
        связанных классов.
        */
        try(Connection connectToBase = DriverManager.
                                            getConnection("url","username","pass")){
            /* Некий код направленный на удаление user-а по ID */
            return true;
        }
    }
}
