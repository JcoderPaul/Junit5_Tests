package com.oldboy.MyMockitoDao;
/*
Пример того, как приблизительно выглядит МОК класс.

Mockito класс расширяет класс UserDao и переопределяет
все его методы максимально простой логикой (как приведено
в примере). Естественно UserDao не может быть final, т.к.
от него наследуются.

Но лучше, естественно, посмотреть сами Mock классы из пакета.
*/
import com.oldboy.DAO.UserDao;

import java.util.HashMap;
import java.util.Map;

public class MyUserDaoMock extends UserDao {
    /*
    Как только мы запросили ID пользователя через *.deleteUser(), используя Mockito:

    Mockito.doReturn(true).when(userDao).deleteUser(PAUL.getId());

    то сразу получили, некий ответ в виде true и номер ID user-a, теперь эти данные будут
    сохранены в некий ассоциативный массив (а в реальном Mockito это List), что-то вроде
    коллекции ответов ANSWER см. код ниже:
    */

    private Map<Integer, Boolean> answer = new HashMap<>();
    /*
    Естественно, что это максимально упрощенное объяснение работы MOCKITO объектов, их
    внутренней структуры и методов. Несложно догадаться, что при обилии классов и методов
    в основном тестируемом коде, для каждого из них пришлось бы создавать подобные заглушки.
    */
    @Override
    public boolean deleteUser(Integer userId) {
        return false;
    }
}
