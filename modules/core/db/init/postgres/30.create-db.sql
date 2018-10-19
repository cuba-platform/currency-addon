--Create scheduled task
insert into SYS_SCHEDULED_TASK (
    ID,

    IS_SINGLETON,

    DEFINED_BY,
    BEAN_NAME,
    METHOD_NAME,

    SCHEDULING_TYPE,
    CRON,

    USER_NAME,

    CREATE_TS,
    CREATED_BY,

    METHOD_PARAMS,
    DESCRIPTION
)
values (
    '65dfa486-8c33-1844-268e-58aa85a4c10e',

    true,

    'B',
    'curraddon_CurrencyRateWorkerMBean',
    'updateCurrenciesRateForToday',

    'C',
    '0 0 4 * * *',

    'admin',

    '2018-09-17 11:23:56',
    'admin',

    '<?xml version="1.0" encoding="UTF-8"?><params/>',
    'Fetch currencies rates from external system'
)^


insert into CURRADDON_CURRENCY_DESCRIPTOR
(
    ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, ACTIVE, SYMBOL, NAME, PRECISION_
)
values (
    '62f31cc9-0603-90a8-7634-aa21f2ff5a11', 3, null, null, '2018-10-17 16:13:05', 'admin', null, null, 'USD', true, 'USD', 'US Dollar', 2
)^
