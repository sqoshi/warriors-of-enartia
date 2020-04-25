drop database project;
create database project;
show databases;
use project;
set foreign_key_checks = 0;
create table armors
(
    id       int auto_increment
        primary key,
    name     varchar(25) not null,
    def      int(11)     NOT NULL,
    class_id int         NOT NULL,
    type     varchar(25) NOT NULL,
    constraint armors_name_uindex
        unique (name)
);
create table helmets
(
    id       int auto_increment
        primary key,
    name     varchar(25) not null,
    def      int(11)     NOT NULL,
    class_id int         NOT NULL,
    type     varchar(25) NOT NULL,
    constraint helmets_name_uindex
        unique (name)
);
CREATE TABLE `shields`
(
    `id`     int(11)     NOT NULL AUTO_INCREMENT,
    `name`   varchar(25) NOT NULL,
    def      int(11)     NOT NULL,
    class_id int         NOT NULL,
    size     varchar(25) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `shields_name_uindex` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

create table weapons
(
    id       int         NOT NULL auto_increment
        primary key,
    name     varchar(25) not null,
    class_id int         NOT NULL,
    att      int(11)     NOT NULL,
    `range`  varchar(25) NOT NULL,
    constraint weapons_name_uindex
        unique (name)
);
create table equipment
(
    heroes_id int not null,
    weapon_id int not null,
    shield_id int not null,
    helmet_id int not null,
    armor_id  int not null,
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
    id       int auto_increment
        primary key,
    user_id  int         not null,
    class_id int         not null,
    name     varchar(25) not null,
    gold     int         not null default 0,
    constraint heroes_classes_id_fk
        foreign key (class_id) references classes (id),
    constraint heroes_users_id_fk
        foreign key (user_id) references users (id)
);

create table classes
(
    id   int auto_increment
        primary key,
    name varchar(25) not null,
    constraint classes_name_uindex
        unique (name)
);
create table users
(
    id       int auto_increment
        primary key,
    login    varchar(10) not null,
    password varchar(10) not null,
    type     int         not null
);
create table dungeons
(
    id   int auto_increment
        primary key,
    name varchar(25) not null,
    def  int(11)     NOT NULL,
    att  int(11)     NOT NULL,
    constraint armors_name_uindex
        unique (name)
);
insert into dungeons(name, att, def)
VALUES ('123', 1, 2),
       ('234', 3, 4),
       ('345', 4, 5);

delimiter //
drop procedure if exists insertWeaponSet;
create procedure insertWeaponSet(IN set_name varchar(25))
BEGIN
    DECLARE finished INTEGER DEFAULT 0;
    DECLARE class_ident INT;
    Declare a varchar(25);
    DECLARE my_first_cursor CURSOR FOR (select id from classes);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;

    OPEN my_first_cursor;
    getPerson:
    LOOP
        FETCH my_first_cursor INTO class_ident;
        IF (select name from classes where id = class_ident) = 'Mage' THEN set a = ' Rod'; end if;
        IF (select name from classes where id = class_ident) = 'Warrior' THEN set a = ' Sword'; end if;
        IF (select name from classes where id = class_ident) = 'Archer' THEN set a = ' Bow'; end if;
        IF finished then leave getPerson; END IF;
        insert into weapons(name, class_id, `range`, att)
        values (CONCAT(set_name, a), class_ident, CONCAT(Floor(RAND() * 12) + 1, ' yards'), Floor(RAND() * 44) + 16);
    END LOOP;
    CLOSE my_first_cursor;
END;
//
delimiter ;
call insertWeaponSet('Black');

delimiter //
drop procedure if exists insertClassSet;
create procedure insertClassSet(IN set_name varchar(25), IN class_name varchar(25))
BEGIN
    declare a int;
    declare b varchar(25);
    declare c varchar(25);
    declare d varchar(25);
    set a = (select id from classes where name = class_name limit 1);
    IF a IS NULL then
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Theres no class like that, firstly add it to classes tab';
    end if;
    IF class_name = 'Mage' THEN
        set b = ' Robe';
        set c = ' Hat';
        set d = ' Book';
    end if;
    IF class_name = 'Warrior' THEN
        set b = ' Armor';
        set c = ' Helmet';
        set d = ' Shield';
    END IF;
    If class_name = 'Archer' THEN
        set b = ' Coverlet';
        set c = ' Band';
        set d = ' Quiver';
    end if;
    INSERT INTO armors(name, class_id, type, def)
    values (CONCAT(set_name, b), a, (case floor(rand() * 2)
                                         when 0 then 'Heavy'
                                         when 1 then 'Light' end), rand() * 64 + 14);
    INSERT INTO helmets(name, class_id, type, def)
    values (CONCAT(set_name, c), a, (case floor(rand() * 2)
                                         when 0 then 'Closed'
                                         when 1 then 'Opened' end), rand() * 64 + 14);

    INSERT INTO shields(name, class_id, size, def)
    values (CONCAT(set_name, d), a, (case floor(rand() * 2)
                                         when 0 then 'Large'
                                         when 1 then 'Small' end), rand() * 64 + 14);
END//
delimiter ;

delimiter //
drop procedure if exists fundamental_class_insert;
create procedure fundamental_class_insert()
BEGIN
    insert into classes(name) values ('Mage'), ('Warrior'), ('Archer');
end //
delimiter ;


delimiter $$
drop trigger if exists logincheck;
CREATE TRIGGER logincheck
    before INSERT
    ON users
    FOR EACH ROW
    IF ((select count(*)
         from users
         where login = new.login) > 0) THEN
        SIGNAL SQLSTATE '45000' SET
            MESSAGE_TEXT = 'This login already exists';
    END if;
$$
delimiter ;

drop procedure if exists deleteClassSet;
create procedure deleteClassSet(IN set_name varchar(25), IN class_name varchar(25))
BEGIN
    declare a int;
    set a = (select id from classes where name = class_name limit 1);
    IF a IS NULL then
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Theres no class like that, firstly add it to classes tab';
    end if;
    delete from armors where name LIKE CONCAT('%', set_name, '%') and class_id = a;
    delete from helmets where name LIKE CONCAT('%', set_name, '%') and class_id = a;
    delete from shields where name LIKE CONCAT('%', set_name, '%') and class_id = a;
end;

delimiter //
START TRANSACTION;
set FOREIGN_KEY_CHECKS = 0;
alter table classes
    AUTO_INCREMENT = 1;
alter table heroes
    AUTO_INCREMENT = 1;
alter table equipment
    AUTO_INCREMENT = 1;
alter table armors
    AUTO_INCREMENT = 1;
alter table weapons
    AUTO_INCREMENT = 1;
alter table helmets
    AUTO_INCREMENT = 1;
alter table shields
    AUTO_INCREMENT = 1;
truncate table classes;
truncate table heroes;
truncate table equipment;
truncate table armors;
truncate table weapons;
truncate table shields;
truncate table helmets;
call fundamental_class_insert();
call insertClassSet('Standard', 'Archer');
call insertClassSet('Standard', 'Mage');
call insertClassSet('Standard', 'Warrior');
call insertWeaponSet('Standard');
call insertClassSet('Magnificient', 'Mage');
call insertClassSet('Turkish', 'Warrior');
call insertClassSet('Gold', 'Archer');
SET foreign_key_checks = 1;
COMMIT;
//
delimiter ;

select * from users;
update users set  type = 3 where id =1;

create trigger wearUpNewHeroes
    after insert
    ON heroes
    FOR EACH ROW
    insert into equipment(heroes_id, weapon_id, shield_id, helmet_id, armor_id)
    VALUES (NEW.id,
            (select id
             from weapons
             where class_id = NEW.class_id
             order by att asc
             limit 1), (select id
                        from shields
                        where class_id = NEW.class_id
                        order by def asc
                        limit 1), (select id
                                   from helmets
                                   where class_id = NEW.class_id
                                   order by def asc
                                   limit 1), (select id
                                              from armors
                                              where class_id = NEW.class_id
                                              order by def asc
                                              limit 1));

