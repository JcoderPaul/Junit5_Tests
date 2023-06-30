package com.oldboy.Extensions;
/*
Простейшая демонстрация работы расширения BeforeAllCallback
*/
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
/*
Нам никто не мешает имплементировать весь список расширений Callbacks
Тут мы реализовали единственные методы двух глобальных расширений, которые
запускаются после всех тестов и до всех тестов соответственно.
*/
public class GlobalExtension implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        /* Естественно реализация методов может быть сложнее */
        System.out.println("GlobalExtensionCallback AfterAll Callback run");
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        System.out.println("GlobalExtensionCallback BeforeAll Callback run");
    }
}
