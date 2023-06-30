package com.oldboy.Validator;

import lombok.Value;

/*
@Value - это создание неизменяемых классов, аналог Data, но для неизменяемых классов.
@Data - аннотация объединяет аннотации @ToString, @Getter, @Setter, @EqualsAndHashCode и
        @RequiredArgsConstructor в одну. Предоставляет весь код, который обычно используется
        в классах моделей, например, геттеры для всех полей, сеттеры для всех нефинальных
        полей, реализацию по умолчанию для toString(), equals() и hashCode(), а также
        конструктор, который инициализирует все поля класса.
*/
@Value(staticConstructor = "of")
public class Error {
    String code;
    String message;
}
