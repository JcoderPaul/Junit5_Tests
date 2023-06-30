package com.oldboy.Util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilTest {
    /*
    Параметризованный тест источником данных будет другой метод данного класса
    *.givePropertyArgForGetTestMethod(). Если обратить внимание, то видно, что
    путь для метода источника данных не содержит знак #, в отличие от примеров
    рассмотренных, например тут: Junit5_lesson_5\src\test\java\com\oldboy\UserServiceTest.java
    поскольку здесь не применяется вложенный тестовый класс.
    */
    @ParameterizedTest
    @MethodSource("givePropertyArgForGetTestMethod")
    /* Сравниваем пару ключ - значение */
    void checkGetMethodTest(String key, String expectVol){
        /*
        Метод по соответствующему ключу (значению аргумента), извлекает значение volume из файла
        свойств application.properties, далее мы это значение сравниваем с полученными из метода
        *.givePropertyArgForGetTestMethod() на эквивалентность. Два поставщика данных - файл и
        метод, ключи, и в тот и в другой передаются одни и те же - значения volume должны совпадать,
        если тест написан правильно и метод *.get() работает верно.
        */
        String actualRes = PropertiesUtil.get(key);
        assertEquals(expectVol, actualRes);
    }
    /* Данные берем из файла src/test/resources/application.properties */
    static Stream<Arguments> givePropertyArgForGetTestMethod(){
        return Stream.of(
                Arguments.of("db.url","jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
                Arguments.of("db.user","sa"),
                Arguments.of("db.password",""),
                Arguments.of("db.driver","org.h2.Driver")
        );
    }
}