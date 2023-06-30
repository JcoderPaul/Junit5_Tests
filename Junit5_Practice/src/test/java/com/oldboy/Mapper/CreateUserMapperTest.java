package com.oldboy.Mapper;

import com.oldboy.DTO.CreateUserDto;
import com.oldboy.Entity.Enum.Gender;
import com.oldboy.Entity.Enum.Role;
import com.oldboy.Entity.User;
import com.oldboy.Mapper.CreateUserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class CreateUserMapperTest {

    private final CreateUserMapper mapperTest = CreateUserMapper.getInstance();

    @Test
    void map() {
        /*
        Данный метод преобразует DTO объект в USER объект поэтому:
        Given - дано на вход, DTO исходник.
        */
        CreateUserDto dtoGiven = CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("2012-02-04").
                role(Role.USER.name()).
                gender(Gender.MALE.name()).
                build();
        /*
        When - когда используем метод *.map(Given DTO), для
        преобразования источника и получения объекта класса User.
        */
        User actualRes = mapperTest.map(dtoGiven);
        /* Создаем эталонный объект класса User */
        User expectedRes = User.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday(LocalDate.of(2012,02,04)).
                role(Role.USER).
                gender(Gender.MALE).
                build();
        /*
        Then - затем сравниваем на эквивалентность преобразованный
        методом *.map() объект DTO, с эталонным объектом User созданным
        через builder.
        */
        Assertions.assertThat(actualRes).isEqualTo(expectedRes);
    }

}