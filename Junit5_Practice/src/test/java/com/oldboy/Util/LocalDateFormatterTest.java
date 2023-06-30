package com.oldboy.Util;
/*
Given – When – Then (GWT):
- Given - описывает что «дано», т.е. состояние системы в начальный момент времени
- When - задает непосредственно триггер, который должен привести к результату, чаще
         всего это какое-то действие пользователя.
- Then - определяет результат этого действия, т.е. является критерием приемки.

Нотация Given — When — Then структурирует процесс составления тестов и дает
                       уверенность в том, что тесты описывают все аспекты
                       поведения системы. Не нужно сидеть и постоянно спрашивать
                       себя: "А какой сценарий я еще не описал?".

Итак, алгоритм такой:
- Определить все состояния, которые могут быть заданы, т.е. все Given.
- Определить все триггеры, т.е. When.
- Определить все Then, что именно может произойти.
- Теперь эти списки надо комбинаторно перемножить.

В результате получается набор тестов.
*/
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateFormatterTest {
    /*
    1. Тестируем метод *.format():
    Метод *.format(), класса LocalDateFormatter может принять два формата данных:
    - верный "yyyy-MM-dd";
    - любой другой не верный, который выкинет исключение в процессе парсинга даты.
    */
    @Test
    void ifFormatMethodTakeValidDataFormatTest(){
        /* Given - дано. Некая дата пришла на вход метода в формате "yyyy-MM-dd" */
        String dataForTest = "2023-06-27";
        /* When - когда. Мы используя наш класс и его метод превратили полученные данные в LocalDate */
        LocalDate actualResult = LocalDateFormatter.format(dataForTest);
        /* Then - тогда (затем). Затем мы сравниваем полученные данные с данными после работы метода */
        Assertions.assertThat(actualResult).isEqualTo(LocalDate.of(2023,06,27));
    }

    @Test
    void shouldThrowExceptionIfFormatMethodTakeNOTValidDateFormatTest(){
        /*
        Если дата придет на вход метода *.format() не в том
        формате, например, со временем, то словим исключение.
        */
        String dataForTest = "2023-06-27 08:34";
        assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(dataForTest));
    }
    /*
    2. Тестируем метод *.isValid():
    Данный метод возвращает boolean (true/false) в зависимости от полученного формата данных.
    Т.е. мы можем накидать список вариантов дат и верных ожидаемых исходов результата работы
    метода isValid().

    Сформируем список (поток) аргументов для параметризированного теста:
    */

    @ParameterizedTest
    @MethodSource("giveValidationParameterForIsValidTestMethod")
    void isValid(String date, boolean expectedResult){
        boolean actualResult = LocalDateFormatter.isValid(date);
        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> giveValidationParameterForIsValidTestMethod(){
        return Stream.of(
                Arguments.of("2023-06-27", true),
                Arguments.arguments("2023-05-23 23:12", false),
                Arguments.of("23-01-2023", false),
                Arguments.of(null, false),
                Arguments.of("", false)
        );
    }
}