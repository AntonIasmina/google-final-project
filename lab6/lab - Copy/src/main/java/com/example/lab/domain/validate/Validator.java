package com.example.lab.domain.validate;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}