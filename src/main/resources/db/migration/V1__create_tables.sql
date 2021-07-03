drop table if exists roles;
drop table if exists users;
drop table if exists tours;
drop table if exists guides;
drop table if exists booking;

create table roles (
    id serial not null primary key,
    name varchar(100) not null
);

create table users (
    id serial not null primary key,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    username varchar(20) not null,
    password varchar(20) not null,
    role_id integer not null,
    foreign key (role_id) references roles(id)
);

create table guides (
    id serial not null primary key,
    name varchar(100) not null
);

create table tours (
    id serial not null primary key,
    price real not null,
    difficulty varchar (100) not null,
    start date not null,
    finish date not null
);

create table bookings (
    id serial not null primary key,
    tour_id integer,
    user_id integer,
    guide_id integer,
    foreign key (tour_id) references tours(id),
    foreign key (user_id) references users(id),
    foreign key (guide_id) references guides(id)
);

alter table users add constraint unique_username unique (username);