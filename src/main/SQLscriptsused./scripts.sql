create table armors
(
    id int auto_increment
        primary key,
    name varchar(25) not null,
    constraint armors_name_uindex
        unique (name)
);
create table helmets
(
    id int auto_increment
        primary key,
    name varchar(25) not null,
    constraint helmets_name_uindex
        unique (name)
);
CREATE TABLE `shields` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(25) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE KEY `shields_name_uindex` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table weapons
(
    id int auto_increment
        primary key,
    name varchar(25) not null,
    constraint weapons_name_uindex
        unique (name)
);
create table equipment
(
    heroes_id int not null,
    weapon_id int not null,
    shield_id int not null,
    helmet_id int not null,
    armor_id int not null,
    constraint equipment_heroes_id_uindex
        unique (heroes_id),
    constraint equipment_armors_id_fk
        foreign key (armor_id) references armors (id),
    constraint equipment_helmets_id_fk
        foreign key (helmet_id) references helmets (id),
    constraint equipment_heroes_id_fk
        foreign key (heroes_id) references heroes (id),
    constraint equipment_shields_id_fk
        foreign key (shield_id) references shields (id),
    constraint equipment_weapons_id_fk
        foreign key (weapon_id) references weapons (id)
);




create table heroes
(
    id int auto_increment
        primary key,
    user_id int not null,
    class_id int not null,
    constraint heroes_classes_id_fk
        foreign key (class_id) references classes (id),
    constraint heroes_users_id_fk
        foreign key (user_id) references users (id)
);

create table classes
(
    id int auto_increment
        primary key,
    name varchar(25) not null,
    constraint classes_name_uindex
        unique (name)
);
create table users
(
    id int auto_increment
        primary key,
    login varchar(10) not null,
    password varchar(10) not null,
    type int not null
);



