package com.oldboy.Extensions;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
/* Расширение состояния */
public class ConditionalExtension implements ExecutionCondition {
    /*
    Например, в данном методе можно задать параметр - стоит или нет
    нам останавливать тот или иной (а может все) тест.
    */
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        /*
        Параметр skip можно перед запуском теста передать в IDE
        в поле VM options, раздела Edit Configuration -> -ea -Dskip
        При такой настройке все тесты будут отменены (skip-нуты)
        */
        return System.getProperty("skip") != null
                ? ConditionEvaluationResult.disabled("Test is skipped")
                : ConditionEvaluationResult.enabled("Test enabled by default");
    }
}
