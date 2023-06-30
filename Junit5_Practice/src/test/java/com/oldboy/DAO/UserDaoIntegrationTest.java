package com.oldboy.DAO;

import com.oldboy.Entity.Enum.Gender;
import com.oldboy.Entity.Enum.Role;
import com.oldboy.Entity.User;
import com.oldboy.integration.IntegrationTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoIntegrationTest extends IntegrationTestBase{

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void findAllTest() {
        /* Дано - given - три пользователя загрузили */
        User user1 = userDao.save(getUserForTest("Paul@arrakis.com"));
        User user2 = userDao.save(getUserForTest("Vladimir@geidiprime.com"));
        User user3 = userDao.save(getUserForTest("Shatdam@salususecundus.com"));
        /* When - когда (и если) - использовали метод UserDao и извлекли Id в отдельные List-ы */
        List<User> actualRes = userDao.findAll();
        List<Integer> userTestId = actualRes.stream().
                map(user -> user.getId()).
                toList();
        /*
        Then - тогда (затем, то) - размеры целевого списка равны
        известному числу и содержимое включает ожидаемые данные
        (см. IntegrationTestBase.java)
        */
        Assertions.assertThat(actualRes).hasSize(8);
        Assertions.assertThat(userTestId).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    void findByIdTest() {
        User userForTest = userDao.save(getUserForTest("irulan@corrinohouse.de"));

        Optional<User> actualRes = userDao.findById(userForTest.getId());

        Assertions.assertThat(actualRes).isPresent();
        Assertions.assertThat(actualRes.get()).isEqualTo(userForTest);
    }

    @Test
    void saveTest() {
        /* Помним, что данный метод переиспользуется в других тестах */
        User userForTest = getUserForTest("FeidRauta@harconen.gdp");
        User actualRes = userDao.save(userForTest);
        /*
        Нам не известно какой идентификатор id сгенерировала БД, и
        теоретически он есть, и если метод работает верно, значит, он
        не null - проверяем.
        */
        assertNotNull(actualRes.getId());
    }

    @Test
    void findByEmailAndPasswordTest() {
        /* Дано - given - помещаем User в базу через метод *.save() */
        User userForTest = userDao.save(getUserForTest("test@test.com"));
        /*
        When - когда (и если) - использовали метод поиска по емайлу
        и паролю, и должны получить некоего user-a, запоминаем его.
        */
        Optional<User> actualRes = userDao.findByEmailAndPassword(userForTest.getEmail(),
                                                                  userForTest.getPassword());
        /*
        Then - тогда (затем, то) - проверяем есть ли пользователь возвращенный
        методом *.findByEmailAndPassword() и проверяем его на эквивалентность с
        эталоном - то, что получили на входе.
        */
        Assertions.assertThat(actualRes).isPresent();
        Assertions.assertThat(actualRes.get()).isEqualTo(userForTest);
    }

    @Test
    void userNotFindByEmailAndPasswordIfHeIsNotInBaseTest() {
        /* Дано - given - помещаем User в базу через метод *.save(), там пароль '342' */
        userDao.save(getUserForTest("test@test.com"));
        /*
        When - когда (и если) - использовали метод поиска по емайлу
        и паролю, при этом емайл применяется исходный (их Given), а
        вот пароль пусть будет '9834' - такого точно в базе нет.
        */
        Optional<User> expectedRes = userDao.
                      findByEmailAndPassword("test@test.com","9834");
        /*
        Then - тогда (затем, то) - поскольку такой пары
        емайла и пароля в базе нет, то вернется пустота.
        */
        Assertions.assertThat(expectedRes).isEmpty();
    }

    @Test
    void deleteExistingEntityTest() {
        User userForTest = userDao.save(getUserForTest("leto@atrides.col"));
        boolean expectedRes = userDao.delete(userForTest.getId());
        assertTrue(expectedRes);
    }

    @Test
    void deleteNotExistingEntityTest() {
        User userForTest = userDao.save(getUserForTest("leto@atrides.col"));
        boolean expectedRes = userDao.delete(userForTest.getId()*100);
        assertFalse(expectedRes);
    }

    @Test
    void updateTest() {
        User userForTest = userDao.save(getUserForTest("leto@atrides.col"));
        userForTest.setName("DukeLeto");
        userForTest.setBirthday(LocalDate.of(10234, 12,12));

        userDao.update(userForTest);

        User updatedUser = userDao.findById(userForTest.getId()).get();
        Assertions.assertThat(updatedUser).isEqualTo(userForTest);
    }
    /*
    id будет возвращать база данных, email уникален
    (unique constraint) его передаем через параметры.
    */
    private static User getUserForTest (String email) {
        return User.builder().
                name("Paul").
                email(email).
                password("342").
                birthday(LocalDate.of(2012, 02, 04)).
                role(Role.ADMIN).
                gender(Gender.MALE).
                build();
    }
}