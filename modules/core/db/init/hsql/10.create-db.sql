-- begin CURRADDON_CURRENCY
create table CURRADDON_CURRENCY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(20) not null,
    SYMBOL varchar(4),
    NAME varchar(255) not null,
    --
    primary key (ID)
)^
-- end CURRADDON_CURRENCY
-- begin CURRADDON_CURRENCY_RATE
create table CURRADDON_CURRENCY_RATE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE_ timestamp not null,
    RATE decimal(19, 6) not null,
    CURRENCY_ID varchar(36) not null,
    TARGET_CURRENCY_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end CURRADDON_CURRENCY_RATE
-- begin CURRADDON_CURRENCY_VALUE
create table CURRADDON_CURRENCY_VALUE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    VALUE_ decimal(19, 2) not null,
    DATE_ timestamp not null,
    CURRENCY_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end CURRADDON_CURRENCY_VALUE
