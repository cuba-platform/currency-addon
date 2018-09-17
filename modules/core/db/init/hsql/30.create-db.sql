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
