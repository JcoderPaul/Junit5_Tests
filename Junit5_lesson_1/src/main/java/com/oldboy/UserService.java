package com.oldboy;
/*
Создаем данный класс и пока у него только один метод *.getAll(),
который возвращает пустую коллекцию (база данных пуста, ее даже
нет еще), но метод существовать может и даже что-то возвращать.

Поскольку мы должны вернуть список неких сущностей (объектов), нам
их надо создать это класс User в папке DTO.
*/
import com.oldboy.DTO.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    /*
    Естественно в реальном проекте мы бы организовали
    связь с базой и проводили все манипуляции с ней.
    */
    private final List<User> testUsersBase = new ArrayList<>();
    public List<User> getAll() {
        return testUsersBase;
    }

    public boolean addUser(User user) {
          return testUsersBase.add(user);
    }
}
