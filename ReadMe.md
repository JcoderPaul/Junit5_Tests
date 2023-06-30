## Изучение фреймворка Junit 5

### * [Junit5_lesson_1](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_lesson_1) * 
Примеры работы с аннотациями внутри тестового класса: 
- @Test 
- @BeforeEach
- @AfterEach 
- @BeforeAll 
- @AfterAll

Пример применения аннотации @TestInstance к тестовому классу, различия между TestInstance.Lifecycle.PER_METHOD и TestInstance.Lifecycle.PER_CLASS (см. [FourthTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_1/src/test/java/com/oldboy/FourthTest.java)).

Изучение методов: 
- *.assertTrue()
- *.assertEquals() 

Папка DOC содержит: 
- структура Junit 5 (см. [Junit 5.jpg](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_1/DOC/Junit%205.jpg))
- принципы TDD (см. [TDD.jpg](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_1/DOC/TDD.jpg) и [SecondTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_1/src/test/java/com/oldboy/SecondTest.java))
- кратко о MavenWrapper (см. [MavenWrapper.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_1/DOC/MavenWrapper.txt))

### * [Junit5_lesson_2](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_lesson_2) *

Применение принципов TDD на простых примерах (см. [TddMethodLess.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_2/src/test/java/com/oldboy/alltests/TddMethodLess.java)). Пример комплексного запуска тестов - Launcher API (см. [AllTestRunnerLauncher.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_2/src/test/java/com/oldboy/AllTestRunnerLauncher.java))

Папка DOC содержит: 
- различия Junit 4 и Junit 5, структура фреймворка (см. [Junit 5.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_2/DOC/Junit%205.txt));

### * [Junit5_lesson_3](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_lesson_3) *

Краткие примеры работы с библиотекой AssertJ (см. [AssertJ.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/DOC/AssertJ.txt)), работа с методом: *.assertThat().

Применение Junit метода *.assertAll()

Пример применения аннотации @Tag (см. [UserServiceTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/src/test/java/com/oldboy/UserServiceTest.java))

Краткое рассмотрение фильтрации тестов при выполнении по тегам (см. [TestRunnerLauncher.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/src/test/java/com/oldboy/TestRunnerLauncher.java))

Папка DOC содержит:

- краткое описание методов [AssertJ](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/DOC/AssertJ.txt) (см. [AssertJ.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_3/DOC/AssertJ.txt));

### * [Junit5_lesson_4](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_lesson_4) *

Исследование методов управления порядком запуска тестов, применение аннотаций:
- @TestMethodOrder(MethodOrderer.*order-type*.class)
- @Order(number)
- @DisplayName("name of method")

Применение вложенных классов, аннотация @Nested (см. [UserServiceTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_4/src/test/java/com/oldboy/UserServiceTest.java)).

Папка DOC содержит:

- краткое описание аннотаций Junit 5 (см. [ShortJupiterAnnotation.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_4/DOC/ShortJupiterAnnotation.txt))

### * [Junit5_lesson_5](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_lesson_5) *

Работа с параметризированными тестами. Исследование аннотаций: 
- @ParameterizedTest
- @NullSource
- @EmptySource
- @NullAndEmptySource
- @ValueSource
- @MethodSource
- @CsvFileSource
- @CsvSource

Тестирование исключений, использование метода: 
- assertThrows

См. подробнее [UserServiceTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_5/src/test/java/com/oldboy/UserServiceTest.java)

### * [Junit5_lesson_6](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_lesson_6) *

Понятие Flaky тест. Применение аннотаций:
- @Disable 
- @RepeatedTest(value, name)
- @TimeOut()

Использование методов: 
- assertTimeout

### * [Junit5_lesson_7](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_lesson_7) * 

Dependency Injection - внедрение зависимостей. Расширение функциональности тестов. Аннотация @ExtendWith. Использование: 
- UserServiceParamResolver.class
- GlobalExtension.class
- PostProcessionExtension.class
- ConditionalExtension.class
- ThrowableExtension.class

Папка DOC содержит: 
- схема внедрения зависимости (см. [DI.jpg](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/DI.jpg))
- жизненный цикл теста (см. [TestLifeCycle.jpg](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/TestLifeCycle.jpg))
- модель расширения (см. [ExtensionModel.jpg](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/ExtensionModel.jpg))
- схема внедрения (см. [Callbacks.jpg](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/Callbacks.jpg))

Cтатьи о принципах внедрения зависимостей: 
- [ArticleAboutDependencies.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/ArticleAboutDependencies.txt)
- [DependencyInjection.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/DependencyInjection.txt)
- [WhenToUseDI.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/WhenToUseDI.txt)
- [DependencyInjectionContainers.txt](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_lesson_7/DOC/DependencyInjectionContainers.txt)

### * [Junit5_Mockito_lesson_8](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_Mockito_lesson_8) *

Применение фреймворка Mockito: 
- Примеры создание MOCK требуемого класса, применение структуры (Mockito.doReturn().when().*some_method*) и (Mockito.when(*some_class*.*some_method*).thenReturn(data)) их отличия (см. [UserServiceMockTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_Mockito_lesson_8/src/test/java/com/oldboy/Service/UserServiceMockTest.java)).
- Пример создания SPY требуемого класса, применение Mockito.when. ... и Mockito.do. ... (см. [UserServiceSpyTest.java](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_Mockito_lesson_8/src/test/java/com/oldboy/Service/UserServiceSpyTest.java))

Папка DOC содержит: 
- схема Mockito дублеров (дубликатов) (см. [MockitoTestDoubles.jpg](https://github.com/JcoderPaul/Junit5_Tests/blob/master/Junit5_Mockito_lesson_8/DOC/MockitoTestDoubles.jpg))

### * [Junit5_Mockito_lesson_9](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_Mockito_lesson_9) * 

Применение фреймворка Mockito: 
- Пример расширений Mockito
- Использование аннотаций: 
  - @Mock
  - @InjectMocks
  - @Captor
- Применение методов: 
  - Mockito.times()
  - Mockito.verify()

### * [Junit5_Practice](https://github.com/JcoderPaul/Junit5_Tests/tree/master/Junit5_Practice) *

Практическое применение Junit 5 и Mockito при покрытии тестами простого проекта (применение Given-When-Then).