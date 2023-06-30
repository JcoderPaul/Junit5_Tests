package com.oldboy.DAO;

import com.oldboy.Entity.Enum.Gender;
import com.oldboy.Entity.Enum.Role;
import com.oldboy.Entity.User;
import com.oldboy.Util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static lombok.AccessLevel.PRIVATE;

/* Наш класс UserDao будет синглтоном */
@NoArgsConstructor(access = PRIVATE)
public class UserDao implements Dao<Integer, User> {

    private static final UserDao INSTANCE = new UserDao();
    /* Задаем необходимый набор SQL запросов: GET_ALL_SQL - выбрать все записи из таблицы users */
    private static final String GET_ALL_SQL = """
            SELECT id,name,birthday,email,password,role,gender
            FROM users
            """;
    /* GET_BY_ID_SQL - выбрать все поля из таблицы users с требуемым id записи */
    private static final String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id = ?";
    /* GET_BY_EMAIL_AND_PASSWORD_SQL - выбрать все поля из таблицы users с
                                       требуемым email и password записи
    */
    private static final String GET_BY_EMAIL_AND_PASSWORD_SQL =
                            GET_ALL_SQL + " WHERE email = ? AND password = ?";
    /* SAVE_SQ - добавить запись в таблицу users с заданными параметрами VALUES */
    private static final String SAVE_SQL =
            "INSERT INTO users (name, birthday, email, password, role, gender) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    /* DELETE_BY_ID_SQL - удалить запись из таблицы users с заданным параметром id */
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
    /* UPDATE_BY_ID_SQL - внести изменения в уже существующую запись
                          из таблицы users с заданным параметром id
    */
    private static final String UPDATE_BY_ID_SQL = """
            UPDATE users
            SET name = ?, 
                birthday = ?,
                email = ?,
                password = ?,
                role = ?,
                gender = ?
            WHERE id = ?
            """;
    public static UserDao getInstance() {
        return INSTANCE;
    }
    /* Метод реализует получение всех записей из таблицы users */
    @Override
    @SneakyThrows
    public List<User> findAll() {
        /* Устанавливаем связь с базой данных и подготовка SQL запроса */
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
             /* Выполняем ответ и получаем ответ в виде Set-a */
             ResultSet resultSet = preparedStatement.executeQuery();
             /* Создаем список куда будут занесены все user-s из коллекции */
             List<User> users = new ArrayList<>();
             /* При наличии следующей записи в коллекции */
             while (resultSet.next()) {
                 /* Добавляем в список user-a, см. метод *.buildEntity() */
                 users.add(buildEntity(resultSet));
             }
             return users;
        }
    }
    /* Получить user-a из базы данных по id */
    @Override
    @SneakyThrows
    public Optional<User> findById(Integer id) {
        /* Устанавливаем связь с БД и формируем запрос */
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.
                                                   prepareStatement(GET_BY_ID_SQL)) {
             /*
             Задаем параметр SQL запроса см.
             https://github.com/JcoderPaul/JDBC_Practice/blob/master/Doc/PreparedStatement_Interface.txt
             или
             https://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
             */
             preparedStatement.setObject(1, id);
             /* Поскольку user-a с таким id может и не быть, работаем с Optional объектом */
             ResultSet resultSet = preparedStatement.executeQuery();
             return resultSet.next()
                    ? Optional.of(buildEntity(resultSet))
                    : Optional.empty();
        }
    }
    /* Сохранить данные user-a в БД и вернуть его уже с id сгенерированным БД */
    @Override
    @SneakyThrows
    public User save(User entity) {
        /* Соединение с БД и подготовка запроса с возможностью получить данные сгенерированные ей же */
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.
                                                   prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {

             prepareStatementToUpsert(preparedStatement, entity);
             /* int executeUpdate() - Выполняет оператор SQL в этом объекте PreparedStatement,
                                      который должен быть оператором языка манипулирования данными
                                      SQL (DML), например INSERT, UPDATE или DELETE; или оператор
                                      SQL, который ничего не возвращает, например оператор DDL.
             */
             preparedStatement.executeUpdate();

             ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
             generatedKeys.next();
             entity.setId(generatedKeys.getObject("id", Integer.class));

             return entity;
        }
    }
    /* Найти user-a по email и password */
    @SneakyThrows
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.
                                                   prepareStatement(GET_BY_EMAIL_AND_PASSWORD_SQL)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            /* Пользователя с таким сочетанием email и password может и не быть в базе */
            return resultSet.next()
                    ? Optional.of(buildEntity(resultSet)) // Если есть билдим его метод см. ниже
                    : Optional.empty(); // Если нет возвращаем пустой
        }
    }
    /* Удалить пользователя из БД по id */
    @Override
    @SneakyThrows
    public boolean delete(Integer id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.
                                                   prepareStatement(DELETE_BY_ID_SQL)) {
             preparedStatement.setObject(1, id);
             /*
             Метод *.executeUpdate() возвращает либо количество строк для операторов SQL DML,
             т.е. нечто больше 0, либо 0 для операторов SQL, которые ничего не возвращают,
             т.е. записей по запросу в соответствующей таблице БД не нашлось
             */
             return preparedStatement.executeUpdate() > 0;
        }
    }
    /* Обновить поля в записи таблицы user */
    @Override
    @SneakyThrows
    public void update(User entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.
                                                   prepareStatement(UPDATE_BY_ID_SQL)) {
            /* Задаем предстоящие изменения и вносим их в PreparedStatement запрос */
            prepareStatementToUpsert(preparedStatement, entity);
            /* Добавляем id к предыдущим данным */
            preparedStatement.setObject(7, entity.getId());
            /* Выполняем изменения */
            preparedStatement.executeUpdate();
        }
    }

    private User buildEntity(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getObject("id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .birthday(resultSet.getObject("birthday", Date.class).toLocalDate())
                .email(resultSet.getObject("email", String.class))
                .password(resultSet.getObject("password", String.class))
                .role(Role.find(resultSet.getObject("role", String.class)).
                                                                            orElse(null))
                .gender(Gender.find(resultSet.getObject("gender", String.class)).
                                                                            orElse(null))
                .build();
    }
    /*
    Текущий метод подготавливает объект PreparedStatement к методу executeUpdate(),
    возможны два варианта:
    - когда мы только сохраняем нашего нового user-a, и тогда мы передаем в SQL
    PreparedStatement все кроме id, который возвращается из БД вместе с ResultSet
    и его мы получаем методом *.getGeneratedKeys() (см. метод save текущего класса).
    - когда мы вносим изменения в уже существующую запись user-a (см. метод update
    текущего класса).
     */
    private void prepareStatementToUpsert(PreparedStatement preparedStatement,
                                                            User entity) throws SQLException {
        preparedStatement.setObject(1, entity.getName());
        preparedStatement.setObject(2, entity.getBirthday());
        preparedStatement.setObject(3, entity.getEmail());
        preparedStatement.setObject(4, entity.getPassword());
        preparedStatement.setObject(5,
                                     entity.getRole() != null ? entity.getRole().name() : null);
        preparedStatement.setObject(6,
                                     entity.getGender() != null ? entity.getGender().name() : null);
    }
}