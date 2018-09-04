package com.haulmont.addon.currency.core;

import com.haulmont.addon.currency.entity.AddonCurrencyValue;
import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.View;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ONE;

@Component(CurrencyAPI.NAME)
public class CurrencyBean implements CurrencyAPI {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyBean.class);

    private final static RoundingMode FINANCIAL_ROUNDING_MODE = RoundingMode.HALF_UP;

    @Inject
    private DataManager dataManager;
    @Inject
    private TimeSource timeSource;
    @Inject
    private CurrencyRateProvider currencyRateProvider;


    @Override
    public Currency getDefaultCurrency() {
        return dataManager.load(Currency.class)
                .query("select c from curraddon$Currency c where c.isDefault = true")
                .one();
    }


    @Override
    public List<Currency> getAllCurrencies() {
        LoadContext<Currency> loadContext = createCurrencyLoadContext("select r from curraddon$Currency r");
        return dataManager.loadList(loadContext);
    }


    @Override
    public List<Currency> getActiveCurrencies() {
        LoadContext<Currency> loadContext = createCurrencyLoadContext("select r from curraddon$Currency r where r.active = true");
        return dataManager.loadList(loadContext);
    }


    protected LoadContext<Currency> createCurrencyLoadContext(String queryString) {
        LoadContext.Query query = new LoadContext.Query(queryString)
                .setCacheable(true);
        return new LoadContext<>(Currency.class)
                .setQuery(query)
                .setView(View.LOCAL);
    }


    @Override
    public BigDecimal convertAmountToCurrentRate(BigDecimal amount, Currency sourceCurrency, Currency targetCurrency) {
        return convertAmount(amount, timeSource.currentTimestamp(), sourceCurrency, targetCurrency);
    }


    @Override
    public BigDecimal convertAmount(AddonCurrencyValue sourceCurrencyValue, Currency targetCurrency) {
        return convertAmount(sourceCurrencyValue.getValue(), sourceCurrencyValue.getDate(), sourceCurrencyValue.getCurrency(), targetCurrency);
    }


    public Currency getCurrencyByCode(String code) {
        Optional<Currency> optionalCurrency = getAllCurrencies().stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst();
        if (optionalCurrency.isPresent()) {
            return optionalCurrency.get();
        } else {
            throw new IllegalStateException(String.format("Currency %s is not found", code));
        }
    }


    @Override
    public BigDecimal convertAmount(BigDecimal amount, Date date, Currency sourceCurrency, Currency targetCurrency) {
        if (amount == null) {
            return null;
        }
        if (sourceCurrency.getCode().equals(targetCurrency.getCode())) {
            return amount;
        }
        CurrencyRate currencyRate = getRate(date, sourceCurrency, targetCurrency);
        if (currencyRate == null) {
            LOG.error(String.format("Currency rate %s/%s is not found for date %s",
                    sourceCurrency.getCode(), targetCurrency.getCode(), date));
            return null;
        }
        return amount.multiply(currencyRate.getRate());
    }


    @Override
    public BigDecimal convertAmountToRateReverse(BigDecimal amount, Date date, Currency sourceCurrency, Currency targetCurrency) {
        if (amount == null) {
            return null;
        }
        if (sourceCurrency.getCode().equals(targetCurrency.getCode())) {
            return amount;
        }
        CurrencyRate currencyRate = getRate(date, targetCurrency, sourceCurrency);
        if (currencyRate == null) {
            LOG.error(String.format("Currency rate %s/%s is not found for date %s",
                    sourceCurrency.getCode(), targetCurrency.getCode(), date));
            return null;
        }
        BigDecimal rate = currencyRate.getRate();
        return amount.multiply(ONE.divide(rate, rate.scale(), FINANCIAL_ROUNDING_MODE));
    }


    protected CurrencyRate getRate(Date date, Currency currency, Currency targetCurrency) {
        CurrencyRate localRate = getLocalRate(date, currency, targetCurrency);
        if (localRate != null) {
            return localRate;
        } else {
            try {
                List<CurrencyRate> currencyRates = currencyRateProvider.getRates(date, currency, Collections.singletonList(targetCurrency));
                if (!currencyRates.isEmpty()) {
                    CurrencyRate currencyRate = currencyRates.get(0);
                    dataManager.commit(currencyRate);
                    return currencyRate;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }


    public CurrencyRate getLocalRate(Date date, Currency sourceCurrency, Currency targetCurrency) {
        List<CurrencyRate> list = dataManager.loadList(new LoadContext<>(CurrencyRate.class)
                .setQuery(new LoadContext.Query("select r from curraddon$CurrencyRate r " +
                        "where r.date <= :date " +
                        "and r.currency.id = :currency " +
                        "and r.targetCurrency.id = :targetCurrency " +
                        "order by r.date desc")
                        .setParameter("date", date)
                        .setParameter("currency", sourceCurrency.getId())
                        .setParameter("targetCurrency", targetCurrency.getId())
                        .setMaxResults(1)
                        .setCacheable(true))
                .setView(View.LOCAL));
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
