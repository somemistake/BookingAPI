insert into roles (name) values ('ROLE_ADMIN'), ('ROLE_USER');

insert into users (first_name, last_name, username, password, role_id) values
('Daniil', 'Malashev', 'daniil', 'admin', 1),
('Lewis', 'Scott', 'lewis', 'user', 2),
('Edward', 'Crock', 'edward', 'user', 2),
('Mathew', 'Allen', 'mathew', 'user', 2),
('Hope', 'Holder', 'hope', 'user', 2),
('Sophie', 'McCartney', 'sophie', 'user', 2),
('Clare', 'Grey', 'clare', 'user', 2),
('Nick', 'Stanford', 'nick', 'user', 2),
('Claus', 'Bridge', 'claus', 'user', 2),
('Sophie', 'Rose', 'sophie1', 'user', 2),
('Peter', 'Norton', 'peter', 'user', 2),
('Harry', 'Potter', 'harry', 'user', 2);

insert into guides (name) values ('Chris'), ('Bill'), ('Kale');

insert into tours (price, difficulty, start, finish) values
(3, 'easy', '2021-04-01', '2021-04-02'),
(4, 'medium', '2021-04-01', '2021-04-02'),
(5, 'hard', '2021-04-01', '2021-04-02'),
(3, 'easy', '2021-05-01', '2021-05-02'),
(4, 'medium', '2021-05-01', '2021-05-02'),
(5, 'hard', '2021-05-01', '2021-05-02');

insert into bookings (tour_id, user_id, guide_id) values
(1, 2, 1),
(1, 3, 1),
(1, 4, 1);