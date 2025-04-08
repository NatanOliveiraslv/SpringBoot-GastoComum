create table user(
    id bigint not null auto_increment,
    login varchar(100) not null,
    senha varchar(100) not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(100) not null,
    primary key (id)
);