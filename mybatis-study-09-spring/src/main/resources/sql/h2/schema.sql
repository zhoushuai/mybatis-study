--------------------------------------------------------------------------------
-- t_person
--------------------------------------------------------------------------------

drop table if exists t_person;
create table t_person
(
    id            bigint      not null auto_increment primary key,
    first_name    varchar(40) not null,
    last_name     varchar(40) not null,
    contact_phone varchar(20),
    contact_addr  varchar(200)
);

