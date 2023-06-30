package com.oldboy.Extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;

public class ThrowableExtension implements TestExecutionExceptionHandler {
    /* Метод расширения по работе с исключениями */
    @Override
    public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        /*
        Например, наш тест выкинул исключение, тогда это расширение
        отреагирует на него если оно будет например только IOException
        остальные тесты пройдут, даже если тоже выкинут исключения, но
        уже другие.
        */
        if (throwable instanceof IOException){
            throw throwable;
        }
    }
}
