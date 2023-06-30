package com.oldboy.Service;
/*
Краткое изучение Mockito.
*/

import com.oldboy.DAO.UserDao;
import com.oldboy.DTO.User;

import java.util.*;

public class UserService {
    /*
    Естественно в реальном проекте мы бы организовали
    связь с базой и проводили все манипуляции с ней.
    */
    private final List<User> testUsersBase = new ArrayList<>();
    private final UserDao userDao;
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    /*
    В данном случае метод *.delete() текущего класса,
    зависит от метода *.deleteUser() класса UserDao.
    Т.е. мы имеем зависимость и при тестировании
    стандартными методами получим интеграционный тест,
    а не юнит тест.
    */
    public boolean delete(Integer userId){
        return userDao.deleteUser(userId);
    }

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
        if (username == null || username == "" ||password == null) {
            throw new IllegalArgumentException("Username or password is null or empty");
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
