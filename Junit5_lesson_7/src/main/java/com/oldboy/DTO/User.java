package com.oldboy.DTO;/*
Подключили Lombok, на этот раз уже через
pom.xml, документация и описание аннотаций:
https://projectlombok.org/features/
*/
import lombok.Value;

@Value (staticConstructor = "of")
public class User {
    Integer id;
    String username;
    String password;
}
