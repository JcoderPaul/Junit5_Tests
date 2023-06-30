package com.oldboy.Validator;

public interface Validator<T> {

    ValidationResult validate(T object);
}
