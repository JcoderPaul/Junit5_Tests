package com.oldboy.Extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class PostProcessionExtension implements TestInstancePostProcessor {
    /*
    Как и в предыдущих интерфейсах Extension Model
    нам нужно реализовать единственный метод. ИМЕННО
    ЭТО РАСШИРЕНИЕ ИСПОЛЬЗУЕТСЯ Spring-ом ДЛЯ ВНЕДРЕНИЯ
    СВОИХ ЗАВИСИМОСТЕЙ.

    Именно в этом callback мы приходим в точку нашего
    тестового класса, когда он только создан и мы можем
    внедрить необходимые зависимости.
    */
    @Override
    public void postProcessTestInstance(Object testInstance,
                                        ExtensionContext extensionContext) throws Exception {
        System.out.println("PostProcessTestInstance method");
        /* Тут мы можем получить все объявленные поля Object и произвести инъекции */
        Field[] declaredField = testInstance.getClass().getDeclaredFields();

    }
}
