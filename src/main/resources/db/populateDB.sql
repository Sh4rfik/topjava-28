DELETE
FROM user_role;
DELETE
FROM users;
DELETE
FROM meals;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100001, '2023-02-20 12:45:00', 'Lunch', 500),
       (100001, '2023-02-20 18:30:00', 'Dinner', 600),
       (100001, '2023-02-20 08:35:00', 'Breakfast', 900),
       (100000, '2023-02-20 19:54:00', 'Dinner', 300),
       (100000, '2023-02-20 12:00:00', 'Lunch', 550);

