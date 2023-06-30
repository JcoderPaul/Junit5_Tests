package com.oldboy.Mapper;

public interface Mapper<F, T> {

    T map(F object);
}
