package com.haulmont.addon.currency.core;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.app.Authenticated;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component(CurrencyRateWorkerMBean.NAME)
public class CurrencyRateWorkerMBeanImpl implements CurrencyRateWorkerMBean {

    @Inject
    private CurrencyAPI currencyAPI;

    @Inject
    private CurrencyRateProvider currencyRateProvider;

    @Inject
    private TimeSource timeSource;

    @Inject
    private Metadata metadata;

    @Inject
    private DataManager dataManager;

    @Inject
    private Logger logger;

    @Override
    @Authenticated
    public void updateCurrencies() {
        updateCurrenciesForDate(timeSource.currentTimestamp());
    }

    @Override
    @Authenticated
    public void updateCurrencies(Date from, Date to) {
        Date start = from;

        while (start.before(to)) {
            updateCurrenciesForDate(start);
            start = DateUtils.addDays(start, 1);
        }

    }

    protected void updateCurrenciesForDate(Date date) {
        CommitContext commitContext = new CommitContext();
        List<Currency> availableCurrencies = currencyAPI.getAvailableCurrencies();
        for (Currency currency : availableCurrencies) {
            List<Currency> targetCurrencies = availableCurrencies.stream()
                    .filter(e -> !e.equals(currency) && currencyAPI.getLocalRate(date, currency, e) == null)
                    .collect(Collectors.toList());
            try {
                List<CurrencyRate> currencyRates = currencyRateProvider.getRates(date, currency, targetCurrencies);
                commitContext.getCommitInstances().addAll(currencyRates);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        dataManager.commit(commitContext);
    }
}
