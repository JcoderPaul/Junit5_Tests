### Порядок тестов в JUnit

По умолчанию JUnit запускает тесты в детерминированном, но непредсказуемом порядке.

В большинстве случаев такое поведение совершенно нормально и приемлемо. Но бывают случаи, когда нам нужно обеспечить соблюдение определенного порядка.

В JUnit 5 мы можем использовать @TestMethodOrder для управления порядком выполнения тестов см. UserServiceTest.java:
- @TestMethodOrder(MethodOrderer.Random.class) - вызов тестов в случайном порядке при каждом запуске тестового класса;
- @TestMethodOrder(MethodOrderer.OrderAnnotation.class) - вызов тестов помеченных аннотацией @Order(n), где n - порядковый номер теста заданный нами  (см. OrderAnatationTest.java);


    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class UserServiceTest {
    
    // некий код (some code)

        @Test
        @Tag("addUserMethod")
        @Order(1)
        void getEmptySizeBaseAfterAddTwoUserTest(){
        // тестовый код (test code)
        }
    
        @Test
        @Tag("convertListToMapMethod")
        @Order(2)
        void convertedUserListToMapById(){
        // тестовый код (test code)
        }

    // некий код (some code)
    
    }

- @TestMethodOrder(MethodOrderer.MethodName.class) - вызов тестов в алфавитном (точнее лексикографическом) порядке;


    @TestMethodOrder(MethodOrderer.MethodName.class)
    public class UserServiceByalphabetTest {

    // некий код (some code)

        @Test
        void aCodeTest(){
        // тестовый код (test code)
        }
    
        @Test
        void bCodeTest(){
        // тестовый код (test code)
        }

        @Test
        void dCodeTest(){
        // тестовый код (test code)
        }
    
        @Test
        void eCodeTest(){
        // тестовый код (test code)
        }

    // некий код (some code)
    
    }

- @TestMethodOrder(MethodOrderer.DisplayName.class) - порядок прохождения тестов по отображаемому имени, оказывается, название теста по умолчанию является и отображаемым, т.е. мы можем используя аннотацию @DisplayName("some_name") задать удобное и понятное имя теста (см. DisplayNameTest.java).

### Вложенные тестовые классы

Например, нам необходимо вынести все тестовые методы, которые работают с методом *.login(name, pass) в отдельный класс, но мы при этом хотим, чтобы этот класс запускался вместе с другими, написанными нами, тестами в текущем классе (хотим получить наглядность отображаемой информации). Данный вложенный класс (аннотация @Nested) также может быть аннотирован, например, как @Tag("login") и @TestMethodOrder(MethodOrderer.DisplayName.class).

Пример см. [UserServiceTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_4/src/test/java/com/oldboy/UserServiceTest.java) (аннотации) - при запуске всех тестов внутри класса, при соответствующей настройке MethodOrder мы получаем необходимое нам отображение результатов тестирования.