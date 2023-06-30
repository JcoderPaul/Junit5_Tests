20.06.2023

### Параметризированные тесты в Junit 5

Для запуска параметризированных тестов необходимо подключить к нашему проекту зависимость junit-jupiter-params.
Мы используем сборщик Maven поэтому на сайте https://mvnrepository.com/ находим 
последнюю версию расширения (зависимости) для добавления в наш pom.xml:

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <version>${junit-version}</version>
        <scope>test</scope>
    </dependency>

### Аннотация @ParameterizedTest

Особенности для всех тестов с параметрами:
- Вместо аннотации @Test необходимо использовать @ParameterizedTest. Такая аннотация позволяет выполнить текущий тест (помеченный @ParameterizedTest) несколько раз, но с разными аргументами.
- Далее для запуска теста мы должны объявить хотя бы один источник аргументов для теста, т.е. некий source-поставщик (провайдер), который предоставит аргументы для каждого вызова нашего помеченного @ParameterizedTest теста.
- Параметр name (хотя и необязательный) аннотации @ParameterizedTest позволяет именовать каждый тест для серии аргументов более наглядно (удобно для восприятия) см. тестовый метод demoCsvGetParameterForTest(String name, String pass) класса UserServiceTest. 
      
      @ParameterizedTest(name = "{arguments} test {index}")
      void demoCsvGetParameterForTest(String name, String pass) {
            // some test code
      }

### Источники тестовых аргументов

Нужно помнить, что большинство документированных провайдеров аргументов, обычно поставляют один параметр. Разновидности аннотаций для передачи аргументов в параметризированный тест:

#### - Аннотация @ValueSource

Пример использования данной аннотации метод loginParametrizedValueSourceTest(String username) см. класс UserServiceTest:

    @ParameterizedTest
    @ValueSource(strings = {
            "Paul", "Vladimir", "Raban"
    })
    void loginParametrizedValueSourceTest(String username){
            // some test code
    }

Предоставленные провайдером параметры: "Paul", "Vladimir", "Raban" последовательно поступают в качестве аргумента в тестовый метод и проверяют работоспособность основного кода программы.

Особенности:
- Провайдер @ValueSource используется примитивов и строк.
- Провайдер определяет массив значений и может использоваться только для предоставления одного аргумента для каждого параметризованного вызова теста.
- Поскольку Java поддерживает боксинг, мы можем использовать литералы в их классах-оболочках.
- В данном провайдере мы не можем передавать null в качестве аргумента даже для типов String и Class (для данных нужд существуют другие провайдеры см. ниже).

Пример:

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    void argsWithAutoboxingParamTest(Integer arg) {
        //test code
    }

#### - Аннотации @NullSource, @EmptySource и @NullAndEmptySource

Аннотация @NullSource предоставляет единственный аргумент 'null' методу аннотированному @ParameterizedTest.

Аннотация @EmptySource предоставляет тестовому методу пустой аргумент следующих типов:
- String
- List
- Set
- Map
- примитивные массивы (например, int [])
- массивы объектов (например, String [])

Аннотация @NullAndEmptySource сочетает в себе функциональность @NullSource и @EmptySource. В примере ниже тестовый метод будет вызываться два раза - сначала со значением null, а затем со значением empty. 

Естественно какие аннотации использовать определяется логикой тестирования.  

В качестве примера см. метод *.loginParametrizedTest(String username) класса UserServiceTest:

    @ParameterizedTest
        /* Предоставляет null параметр */
    @NullSource
        /* Предоставляет пустой параметр или "" */
    @EmptySource
        /* Аннотация, которая объединяет две первые: @NullAndEmptySource */
    void loginParametrizedTest(String username){
        // some test code
    }

Примечание: Поскольку аннотация @ValueSource не поддерживает значение null, используя @NullSource и @EmptySource в аннотации @ValueSource, мы можем тестировать значения null, non-null и empty в одном тесте.

#### - Аннотация @EnumSource

Аннотация @EnumSource позволяет использовать Enum константы в качестве аргументов теста. Тест метод будет вызываться для каждой константы перечисления.

В примере ниже тестовый метод будет вызываться 4 раза, по одному разу для каждой Enum константы.

    enum Direction {
        EAST, WEST, NORTH, SOUTH
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testWithEnumSource(Direction d) {
        // some test code
    }

#### - Аннотация @MethodSource

Одна из самых интересных аннотаций т.к. дает большой масштаб для маневра разработчику тестов.

Особенности:
- Аннотация используется для ссылки на один или несколько фабричных методов тестового класса или внешних классов. Фабричный метод должен генерировать поток аргументов, где КАЖДЫЙ АРГУМЕНТ в потоке будет использоваться методом, аннотированным @ParameterizedTest.
- Фабричный метод должен быть static, если тестовый класс не аннотирован с помощью @TestInstance(Lifecycle.PER_CLASS).
- Сам фабричный метод не должен принимать аргументы.

    
    // Тестовый метод
    @ParameterizedTest
    @MethodSource("getArgsForParamLoginTest")
    void loginParametrizedWithMethodSourceTest(String name,
                                               String pass,
                                               Optional<User> user){
        // some test code
    }
    
    // Провайдер аргументов
    static Stream<Arguments> getArgsForParamLoginTest(){
    return Stream.of(
        Arguments.of("Paul", "123", Optional.of(PAUL)),
        Arguments.of("Vladimir", "321", Optional.of(VLADIMIR)),
        Arguments.of("Raban","987", Optional.of(RABAN)),
        Arguments.of("Paul", "not_right_pass", Optional.empty()),
        Arguments.of("not_right_name","123", Optional.empty())
        );
    }

Если мы явно не предоставим имя фабричного метода (провайдер ресурсов) через @MethodSource, JUnit будет искать фабричный метод, имя которого по умолчанию совпадает с именем текущего метода с аннотацией @ParameterizedTest.

Поэтому, в примере, если мы не предоставим имя метода argsProviderFactory в аннотации @MethodSource, Junit будет искать имя метода testWithMethodSource с возвращаемым типом Stream<String>.

    @ParameterizedTest
    @MethodSource("argsProviderFactory")
    void testWithMethodSource(int argument) {
        // some test code
    }

    static IntStream argsProviderFactory() {
        // data provider code
    }

#### - Аннотация @CsvSource и параметризация нескольких аргументов

Эта аннотация позволяет нам задавать списки аргументов как значения, разделенные запятыми. Каждый CSV токен представляет собой строку CSV и приводит к одному вызову параметризованного теста.

    @ParameterizedTest(name = "{arguments} test {index}")
    @CsvSource({
        "Paul,123",
        "Vladimir,321",
        "Raban,987",
        "Feid,257"
    })
    void demoCsvGetParameterForTest(String name, String pass) {
        // some code for test
    }

#### - Аннотация @CsvFileSource

Данная аннотация похожа на @CsvSource за исключением того, что мы читаем токены CSV из файла вместо чтения токенов в исходном тексте. CSV файл можно прочитать по classpath или из локальной файловой системы.

Разделителем по умолчанию является запятая (,), но мы можем использовать другой символ, установив атрибут разделителя.

Обратите внимание, что любая строка, начинающаяся с символа #, будет интерпретироваться как комментарий и игнорироваться.

    @ParameterizedTest(name = "{arguments} test {index}")
    @CsvFileSource(resources = "/login-pass-data-for-test.csv", // адрес файла
                   delimiter = ',', // разделитель
                   numLinesToSkip = 1) // отступ от начала файла
    void demoCsvGetParameterForTest(String name, String pass) {
       // some test code
    }

Файл ресурсов можно посмотреть в [src/test/resources/login-pass-data-for-test.csv](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_5/src/test/resources/login-pass-data-for-test.csv)

Все аннотации применялись в тех или иных тестах класса [UserServiceTest](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_5/src/test/java/com/oldboy/UserServiceTest.java). 