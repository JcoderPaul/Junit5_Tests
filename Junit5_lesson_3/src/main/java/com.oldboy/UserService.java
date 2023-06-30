package com.oldboy;
/*
Краткое изучение библиотеки AssertJ, методику TDD
продолжаем использовать - сначала тесты, затем
функционал.
*/
import com.oldboy.DTO.User;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

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

    /* Добавим метод списочного добавления пользователей в БД (список) */
    public void addMoreOneUsers(User... users){
        this.testUsersBase.addAll(Arrays.asList(users));
    }

    public Optional<User> login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password is null");
        }
        return testUsersBase.stream().
                filter(userFromBase -> userFromBase.getUsername().equals(username)).
                filter(userFromBase -> userFromBase.getPassword().equals(password)).
                findFirst();
    }
    /*
    Метод создается уже после того, как создан тест на него и мы
    предполагаем, какие результирующие данные на выходе нам ждать.
    */
    public Map<Integer, User> getAllUsersAndConvertToMapById() {
        /*
        Более короткий способ преобразования - это stream API:
        return testUsersBase.stream().
                collect(toMap(User::getId, Function.identity()));
        */
        Map<Integer, User> map = new HashMap<>();
        for (User user : testUsersBase) {
            if (map.put(user.getId(), user) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        return map;
    }
}
