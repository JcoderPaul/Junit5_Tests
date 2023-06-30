package com.oldboy.Util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Properties;

@UtilityClass
public final class PropertiesUtil {

    private static final Properties properties = new Properties();
    /* Статический приватный блок */
    static {
        loadProperties();
    }
    /* Статический приватный метод */
    @SneakyThrows
    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.
                                              getClassLoader().
                                         getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        }
    }
    /*
    Первые два блока кода данного класса приватные и загружается в память JVM
    в момент загрузки самого класса, значит мы можем покрыть тестами, только
    текущий публичный метод *.get()
    */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
