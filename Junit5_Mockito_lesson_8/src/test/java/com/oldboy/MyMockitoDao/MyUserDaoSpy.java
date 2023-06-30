package com.oldboy.MyMockitoDao;

import com.oldboy.DAO.UserDao;

import java.util.HashMap;
import java.util.Map;

public class MyUserDaoSpy extends UserDao {
    /*
    Как только мы запросили ID пользователя через *.deleteUser(), используя Mockito:

    Mockito.doReturn(true).when(userDao).deleteUser(PAUL.getId());

    то сразу получили, некий ответ в виде true и номер ID user-a, теперь эти данные будут
    сохранены в некий ассоциативный массив (а в реальном Mockito это List), что-то вроде
    коллекции ответов ANSWER см. код ниже:
    */
    private Map<Integer, Boolean> answer = new HashMap<>();
    /*
    Spy отличается тем, что реально содержит объект UserDao и
    принимает его в параметрах конструктора.
    */
    private final UserDao userDao;

    public MyUserDaoSpy(UserDao userDao) {
        this.userDao = userDao;
    }
    /*
    При этом вызов метода *.userDao.deleteUser() должен быть
    ленивым и не вызываться при наличии существующего answer
    */
    @Override
    public boolean deleteUser(Integer userId) {
        return answer.getOrDefault(userId, userDao.deleteUser(userId));
    }
}
