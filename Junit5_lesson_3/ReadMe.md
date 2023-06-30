### Немного об AssertJ

Несколько методов в классе [UserServiceTest](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/src/test/java/com/oldboy/UserServiceTest.java) применяют библиотеку [AssertJ](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/DOC/AssertJ.txt), в качестве ознакомления. Небольшое описание см. в [DOC/AssertJ.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/DOC/AssertJ.txt).  

### Исключения в Junit 5

Методы throwExceptionIfUsernameIsNull() и throwExceptionInAllSituationOnLoginNullableEnter() в классе [UserServiceTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/src/test/java/com/oldboy/UserServiceTest.java) демонстрируют возможности работы с исключениями, которые пробрасываются методом *.login(name, pass) класса [UserService.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/src/main/java/com.oldboy/UserService.java) (см. короткие комментарии в методах).

### Теги и фильтры в Junit 5

Очень часто наши тесты JUnit выполняются автоматически как часть непрерывной интегрированной сборки CI с использованием Maven. Однако на это часто уходит много времени.

Поэтому часто возникает желание (необходимость) фильтровать наши тесты и выполнять какой-то их набор (а возможно только один из них), например только модульные тесты, либо интеграционные тесты, либо и то, и другое на разных этапах процесса сборки.

Тут мы рассмотрим, очень кратко, теги и методы фильтрации для тест в JUnit 5.

В JUnit 5 мы можем фильтровать тесты, помечая их подмножество под уникальным именем тега. Например, предположим, что у нас есть как unit тесты @Tag("fastTest"), так и интеграционные тесты @Tag("integrationTest"). Причем теги могут идти один за другим. <br><br>Мы можем добавить теги к обоим наборам тестовых случаев см пример:

    @Test
    @Tag("integrationTest")
    public void testAddUserInToBase() {
    }

    @Test
    @Tag("fastTest")
    public void getUserNameFromRegForm() {
    }

Добавим теги к тестовым методам которые используются в см. класс UserServiceTest.java:
- @Tag("convertListToMapMethod") - помечаем метод тестирующий конвертацию List в Map;
- @Tag("addUserMethod") - помечаем метод тестирующий добавление пользователя в БД (список);
- @Tag("login") - помечаем тестовый метод проверяющий работоспособность метод *.login(name, pass) класса UserService (их больше всего);

Теперь мы можем выполнять все тесты под определенным именем тега отдельно. Мы также можем пометить сразу весь тестовый класс вместо методов, тем самым выделяя все тесты в классе см. UserServiceTest.java.

Фильтрация и запуск тестов возможны при помощи Launcher - a, делается в builder см. TestRunnerLauncher.java, где мы можем добавлять или исключать тесты (без фактического их изменения) из процесса тестирования.

Фильтрация тегов с помощью плагина Maven Surefire

Для фильтрации тестов JUnit на различных этапах сборки Maven мы можем использовать плагин Maven Surefire. Плагин Surefire позволяет нам включать или исключать теги в конфигурации плагина :

    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <groups>fastTest</groups>
    </configuration>
 
При такой настройке будут выполнены все тесты, помеченные как fastTest. Точно так же мы можем исключить тестовые методы (классы) под именем тега:

    <excludedGroups>integrationTest</excludedGroups>    
