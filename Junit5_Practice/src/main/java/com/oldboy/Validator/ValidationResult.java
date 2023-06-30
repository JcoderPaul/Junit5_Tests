package com.oldboy.Validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    @Getter
    private final List<Error> errors = new ArrayList<>();

    public void add(Error error) {
        this.errors.add(error);
    }
    /*
    Если посмотреть на этот метод данного класса в первоначальном его виде
    **********************************************************************
    public boolean isValid(){
        return errors.isEmpty();
    }
    **********************************************************************
    становится понятно, что суть не изменилась, в обоих случаях мы проверяем
    список ошибок на их наличие (пустой/не пустой). Однако два метода isValid()
    у разных классов, в текущем задании слегка путает - поэтому были внесены
    изменения.
    */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}