package com.haulmont.addon.currency.core;

import com.haulmont.addon.currency.core.config.CurrencyApplicationProperties;
import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.service.ConvertResult;
import com.haulmont.cuba.core.global.*;
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

@Component(Currencies.NAME)
public class CurrenciesBean implements Currencies {
    private static final Logger LOG = LoggerFactory.getLogger(CurrenciesBean.class);

    private final static RoundingMode FINANCIAL_ROUNDING_MODE = RoundingMode.HALF_UP;

    @Inject
    private DataManager dataManager;
    @Inject
    private TimeSource timeSource;
    @Inject
    private CurrencyRateProvider currencyRateProvider;
    @Inject
    private Configuration configuration;

    @Override
    public CurrencyDescriptor getDefaultCurrency() {
        String defaultCurrencyCode = configuration.getConfig(CurrencyApplicationProperties.class).getDefaultCurrencyCode();
        return getCurrencyByCode(defaultCurrencyCode);
    }


    @Override
    public List<CurrencyDescriptor> getAllCurrencies() {
        LoadContext<CurrencyDescriptor> loadContext = createCurrencyLoadContext("select r from curraddon$CurrencyDescriptor r");
        return dataManager.loadList(loadContext);
    }


    @Override
    public List<CurrencyDescriptor> getActiveCurrencies() {
        LoadContext<CurrencyDescriptor> loadContext = createCurrencyLoadContext("select r from curraddon$CurrencyDescriptor r where r.active = true");
        return dataManager.loadList(loadContext);
    }


    protected LoadContext<CurrencyDescriptor> createCurrencyLoadContext(String queryString) {
        LoadContext.Query query = new LoadContext.Query(queryString)
                .setCacheable(true);
        return new LoadContext<>(CurrencyDescriptor.class)
                .setQuery(query)
                .setView(View.LOCAL);
    }


    @Override
    public ConvertResult convertAmountToCurrentRate(BigDecimal amount, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency) {
        return convertAmount(amount, timeSource.currentTimestamp(), sourceCurrency, targetCurrency);
    }


    @Override
    public ConvertResult convertAmount(CurrencyRateAware sourceCurrencyValue, CurrencyDescriptor targetCurrency) {
        return convertAmount(sourceCurrencyValue.getValue(), sourceCurrencyValue.getDate(), sourceCurrencyValue.getCurrency(), targetCurrency);
    }


    @Override
    public CurrencyDescriptor getCurrencyByCode(String code) {
        Optional<CurrencyDescriptor> optionalCurrency = getAllCurrencies().stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst();
        if (optionalCurrency.isPresent()) {
            return optionalCurrency.get();
        } else {
            throw new IllegalStateException(String.format("Currency %s is not found", code));
        }
    }


    @Override
    public ConvertResult convertAmount(BigDecimal amount, Date date, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency) {
        if (amount == null) {
            return null;
        }
        if (sourceCurrency.getCode().equals(targetCurrency.getCode())) {
            return new ConvertResult(null, amount);
        }
        CurrencyRate currencyRate = getRate(date, sourceCurrency, targetCurrency);

        if (currencyRate == null) {
            LOG.error("Currency rate {}/{} is not found for date {}", sourceCurrency.getCode(), targetCurrency.getCode(), date);
            return null;
        }

        BigDecimal resultAMount = amount.multiply(currencyRate.getRate());
        return new ConvertResult(currencyRate, resultAMount);
    }


    @Override
    public ConvertResult convertAmountToRateReverse(
            BigDecimal amount, Date date, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency
    ) {
        if (amount == null) {
            return null;
        }
        if (sourceCurrency.getCode().equals(targetCurrency.getCode())) {
            return new ConvertResult(null, amount);
        }
        CurrencyRate currencyRate = getRate(date, targetCurrency, sourceCurrency);
        if (currencyRate == null) {
            LOG.error(String.format("Currency rate %s/%s is not found for date %s",
                    sourceCurrency.getCode(), targetCurrency.getCode(), date));
            return null;
        }
        BigDecimal rate = currencyRate.getRate();
        BigDecimal resultAmount = amount.multiply(ONE.divide(rate, rate.scale(), FINANCIAL_ROUNDING_MODE));

        return new ConvertResult(currencyRate, resultAmount);
    }


    protected CurrencyRate getRate(Date date, CurrencyDescriptor currency, CurrencyDescriptor targetCurrency) {
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


    @Override
    public CurrencyRate getLocalRate(Date date, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency) {
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
