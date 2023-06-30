package com.oldboy;
/*
https://junit.org/junit5/docs/current/api/org.junit.platform.launcher/org/junit/platform/launcher/core/LauncherFactory.html
https://junit.org/junit5/docs/current/api/org.junit.platform.launcher/org/junit/platform/launcher/listeners/SummaryGeneratingListener.html
https://junit.org/junit5/docs/current/api/org.junit.platform.launcher/org/junit/platform/launcher/LauncherDiscoveryRequest.html
*/
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TagFilter;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

public class TestRunnerLauncher {

    public static void main(String[] args) {
        /* Создаем ланчер */
        Launcher firstLauncher = LauncherFactory.create();
        /*
        Поскольку мы пытаемся автоматизировать запуск всех наших
        тестов нам нужно прослушивать модуль, который будет их
        искать по указанному пути (способы указания пути различны)

        И только через лиссенер мы можем посмотреть результаты
        выполнения всех наших тестов (при желании вывести на экран).
        */
        SummaryGeneratingListener summaryGeneratingListener =
                new SummaryGeneratingListener();
        /*
        Наш ланчер поисковик ищет местоположение наших тестов,
        мы указали пакет. Вариантов указания местоположения тестов
        очень велико, вплоть до конкретного файла.
        */
        LauncherDiscoveryRequest launcherDiscoveryRequest =
                LauncherDiscoveryRequestBuilder.
                        request().
                        selectors(DiscoverySelectors.selectPackage("com.oldboy")).
                        /*
                        Добавляем фильтрацию по 'include' - только
                        выбранные или 'exclude' - исключая выбранные:
                        - filters(TagFilter.includeTags("login")).
                        - filters(TagFilter.excludeTags("login")).
                        */
                        filters(TagFilter.excludeTags("login")).
                        build();
        /* Запускаем все наши тесты из найденных */
        firstLauncher.execute(launcherDiscoveryRequest, summaryGeneratingListener);

        try(PrintWriter writeToScreen = new PrintWriter(System.out)){
            summaryGeneratingListener.getSummary().printTo(writeToScreen);
        }
    }
}
