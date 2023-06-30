package com.oldboy.Validator;

import com.oldboy.DTO.CreateUserDto;
import com.oldboy.Entity.Enum.Gender;
import com.oldboy.Entity.Enum.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserValidatorTest {

    private final CreateUserValidator testValidator = CreateUserValidator.getInstance();
    /*
    В данном тесте мы предоставляем DTO user-a с правильной структурой
    без ошибочных полей данных и значит, что он должен пройти тест,
    используя методы тестируемого класса
    */
    @Test
    void shouldPassValidationTest(){
        CreateUserDto userDto = CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("2012-02-04").
                role(Role.USER.name()).
                gender(Gender.MALE.name()).
                build();

        ValidationResult actualRes = testValidator.validate(userDto);
        /*
        Метод *.hasErrors() класса ValidationResult, возвращает false - если ошибок
        валидации нет, и true - если ошибки есть. Отсюда ожидаемый результат - данные
        корректны - ошибок нет и actualRes.hasErrors() возвращает false
        */
        assertFalse(actualRes.hasErrors());
    }

    @Test
    void invalidBirthdayTest(){
        CreateUserDto userDto = CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("20-11-2004").
                role(Role.USER.name()).
                gender(Gender.MALE.name()).
                build();

        ValidationResult actualRes = testValidator.validate(userDto);
        /*
        Вносим ошибку в дату дня рождения, что приведет к помещению ошибки в
        список ошибок 'errors' класса ValidationResult. Мы извлекаем список
        (@Getter) и проверяем его размер - ошибок должно быть не более одной.

        Помним, что класс Errors помечен, как @Value (см. класс)
        */
        Assertions.assertThat(actualRes.getErrors()).hasSize(1);
        /*
        Далее логика почти такая же, что и ранее - извлекаем список ошибок и
        получаем первый элемент списка (индекс ноль) - это класс Error,
        извлекаем его "code", который сами же и поместим туда в случае
        возникновения проблем (см. тестируемый класс) с введением ошибочного
        дня рождения, и просто сравниваем их на эквивалентность.
        */
        Assertions.assertThat(actualRes.getErrors().
                                        get(0).
                                        getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    void invalidGenderTest(){
        CreateUserDto userDto = CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("1985-04-08").
                role(Role.USER.name()).
                gender("QUEER").
                build();

        ValidationResult actualRes = testValidator.validate(userDto);
        /*
        Вносим ошибку в пол (их ведь уже аж 51 шт., а мы учли только два),
        что приведет к помещению ошибки в список ошибок 'errors' класса
        ValidationResult. Мы извлекаем список (@Getter) и проверяем его
        размер - ошибок должно быть ровно одна шт..
        */
        Assertions.assertThat(actualRes.getErrors()).hasSize(1);
        /*
        Далее логика почти такая же, что и ранее - извлекаем список ошибок и
        получаем первый элемент списка (индекс ноль), и извлекаем его "code",
        который сами же и поместим туда в случае возникновения проблем с
        полом, и теперь просто сравниваем их на эквивалентность.

        Еще раз, помним, что класс Errors помечен, как @Value (см. класс) и
        при его извлечении доступны все его Getter-ы.
        */
        Assertions.assertThat(actualRes.getErrors().
                                        get(0).
                                        getCode()).isEqualTo("invalid.gender");
    }

    @Test
    void invalidRoleTest(){
        CreateUserDto userDto = CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("1985-04-08").
                role("WishMaker").
                gender(Gender.MALE.name()).
                build();
        /* Логика та же, что и в предыдущих двух тестах см. выше */
        ValidationResult actualRes = testValidator.validate(userDto);
        Assertions.assertThat(actualRes.getErrors()).hasSize(1);
        Assertions.assertThat(actualRes.getErrors().
                                        get(0).
                                        getCode()).isEqualTo("invalid.role");
    }

    @Test
    void invalidBirthdayGenderRoleTest(){
        /* Given - ДАНО на входе */
        CreateUserDto userDto = CreateUserDto.builder().
                name("Paul").
                email("paul@arrakis.com").
                password("342").
                birthday("1985-04-08 12:23"). // Не валидный
                role("WishMaker"). // Не валидный
                gender("QUEER"). // Не валидный
                build();
        /*
        When - когда используем метод *.validate(ДАНО)
        Логика та же, что и в предыдущих двух тестах
        см. выше
        */
        ValidationResult actualRes = testValidator.validate(userDto);
        /*
        Then - тогда мы получаем следующее:
        - нам должно прилететь в List три ошибки - проверяем;
        */
        Assertions.assertThat(actualRes.getErrors()).hasSize(3);
        /*
        - собираем все коды ошибок в строчный список
          и получаем полный список прилетевших ошибок;
        */
        List<String> codeOfErrors = actualRes.
                getErrors().
                stream().
                map(error -> error.getCode()).
                toList();
        /* - проверяем соответствие списка ошибок, ожидаемому */
        Assertions.assertThat(codeOfErrors).contains("invalid.role",
                                                     "invalid.gender",
                                                     "invalid.birthday");
    }
}