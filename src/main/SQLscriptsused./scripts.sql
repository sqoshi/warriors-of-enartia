create table users
(
    id       int auto_increment
        primary key,
    login    varchar(10) not null,
    password varchar(10) not null,
    type     int         not null
);

create table classes
(
    id   int auto_increment
        primary key,
    name varchar(25) not null,
    constraint classes_name_uindex
        unique (name)
);

create table shields
(
    id   int auto_increment
        primary key,
    name varchar(25) not null,
    constraint shields_name_uindex
        unique (name)
);
