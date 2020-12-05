-- -------------------------------------------------------
-- student table
-- -------------------------------------------------------
drop table if exists student;
create table student
(
    id         bigint      not null primary key auto_increment,
    first_name varchar(40) not null,
    last_name  varchar(40) not null,
    addr       varchar(200)
);


