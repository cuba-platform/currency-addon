package com.haulmont.addon.currency.core;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CurrencyRateWorker {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyRateWorker.class);

    @Inject
    private Currencies currencies;
    @Inject
    private CurrencyRateProvider currencyRateProvider;
    @Inject
    private TimeSource timeSource;
    @Inject
    private DataManager dataManager;
    @Inject
    private Persistence persistence;


    public void updateCurrenciesRateForToday() {
        updateCurrenciesForDate(timeSource.currentTimestamp());
    }


    public void updateCurrenciesRate(Date from, Date to) {
        Date start = from;

        while (!start.after(to)) {
            updateCurrenciesForDate(start);
            start = DateUtils.addDays(start, 1);
        }

    }


    public void removeRatesOlderWhen(long days) {
        Date keepRecordsBorder = new Date();
        keepRecordsBorder.setTime(keepRecordsBorder.getTime() - TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS));

        List<CurrencyRate> rates = dataManager.load(CurrencyRate.class)
                .query("select r from curraddon$CurrencyRate r where r.date < :keepRecordsBorder")
                .parameter("keepRecordsBorder", keepRecordsBorder)
                .list();
        EntityManager entityManager = persistence.getEntityManager();

        rates.forEach(entityManager::remove);
    }


    private void updateCurrenciesForDate(Date date) {
        CommitContext commitContext = new CommitContext();
        Collection<Entity> commitInstances = commitContext.getCommitInstances();

        List<CurrencyDescriptor> availableCurrencies = currencies.getActiveCurrencies();
        for (CurrencyDescriptor currency : availableCurrencies) {
            LOG.debug("Update rate for {} at {}", currency.getCode(), date);

            List<CurrencyDescriptor> targetCurrencies = availableCurrencies.stream()
                    .filter(e -> !e.equals(currency) && currencies.getLocalRate(date, currency, e) == null)
                    .collect(Collectors.toList());
            try {
                List<CurrencyRate> currencyRates = currencyRateProvider.getRates(date, currency, targetCurrencies);
                commitInstances.addAll(currencyRates);
            } catch (Exception e) {
                LOG.error("Can't fetch rates for {} at {} for converting to {} from external service", currency, date, targetCurrencies, e);
            }
        }
        dataManager.commit(commitContext);
    }
}
