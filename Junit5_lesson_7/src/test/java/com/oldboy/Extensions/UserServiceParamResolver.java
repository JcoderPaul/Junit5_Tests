package com.oldboy.Extensions;

import com.oldboy.Service.UserService;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UserServiceParamResolver implements ParameterResolver {
    /*
    Метод supportsParameter определяет, подходит ли
    предоставленный объект для внедрения и если подходит,
    то в работу вступает resolveParameter.

    ParameterContext предоставляет всю информацию об объекте,
    который мы в дальнейшем внедрим в требуемый тест или нет.
    */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext)
                                                    throws ParameterResolutionException {
        /*
        Получаем из parameterContext параметры и тип
        переданного объекта, если он является требуемого
        класса, то возвращаем true и внедряем его.
        */
        return parameterContext.getParameter().getType() == UserService.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext)
                                                    throws ParameterResolutionException {
        /*
        Да код простой, но это демонстрация принципов DI в Junit 5
        Создаем новый объект UserService для передачи его в тестовый
        метод помеченный аннотацией @BeforeEach.
        */

        // return new UserService();

        /*
        Можно развить тему и усложнить код.

        Мы можем закешировать наш UserService и для этого есть готовый
        инструмент Store, который можно воспринимать как HashMap. И в
        данном случае нам будет возвращаться КАЖДЫЙ РАЗ ОДИН И ТОТ ЖЕ
        объект UserService, поскольку в метод *.create() в качестве
        параметра передан UserService.class.

        Если мы хотим, чтобы для каждого тестового метода создавался свой
        объект UserService, то в *.create() можно передать метод, например:

        ExtensionContext.Store myStore = extensionContext.
                                       getStore(ExtensionContext.
                                                       Namespace.
                                                       create(extensionContext.getTestMethod()));
        */
        ExtensionContext.Store myStore = extensionContext.
                                       getStore(ExtensionContext.
                                                       Namespace.
                                                       create(UserService.class));
        /* По ключу UserService.class мы возвращаем new UserService() */
        return myStore.getOrComputeIfAbsent(UserService.class, it -> new UserService());
    }
}
