package com.oldboy;

/*
В pom.xml прописаны необходимые настойки зависимостей
Jupiter для Junit5 и подключены нужные плагины Surefire.
*/

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/*
Постфикс или суффикс "Test" указывает
системе, что данный класс тестовый. В
данном случае тест заведомо провальный.
*/
public class FirstTest {
    @Test
    void simpleTest(){
        assertTrue(false);
    }
}
