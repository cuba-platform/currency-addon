package com.haulmont.addon.currency.core;

import com.haulmont.cuba.security.app.Authenticated;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;


@Component(CurrencyRateWorkerMBean.NAME)
public class CurrencyRateWorkerMBeanImpl implements CurrencyRateWorkerMBean {

    @Inject
    private CurrencyRateWorker currencyRateWorker;


    @Override
    @Authenticated
    public void updateCurrenciesRateForToday() {
        currencyRateWorker.updateCurrenciesRateForToday();
    }


    @Override
    @Authenticated
    public void updateCurrenciesRate(Date from, Date to) {
        currencyRateWorker.updateCurrenciesRate(from, to);
    }


    @Override
    @Authenticated
    @Transactional
    public void removeRatesOlderWhen(long days) {
        currencyRateWorker.removeRatesOlderWhen(days);
    }

}
