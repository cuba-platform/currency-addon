-- begin CURRADDON_CURRENCY_DESCRIPTOR
create table CURRADDON_CURRENCY_DESCRIPTOR (
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
    ACTIVE boolean not null,
    SYMBOL varchar(4),
    NAME varchar(255) not null,
    PRECISION_ integer not null,
    --
    primary key (ID)
)^
-- end CURRADDON_CURRENCY_DESCRIPTOR
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
    RATE decimal(19, 12) not null,
    CURRENCY_ID varchar(36) not null,
    TARGET_CURRENCY_ID varchar(36) not null,
    SOURCE varchar(100) not null,
    --
    primary key (ID)
)^
-- end CURRADDON_CURRENCY_RATE
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
    VALUE_ decimal(19, 12) not null,
    DATE_ timestamp not null,
    CURRENCY_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end CURRADDON_CURRENCY
