package com.haulmont.addon.currency.core.impl;

import com.haulmont.addon.currency.core.CurrencyRateProvider;
import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(CurrencyRateProvider.NAME)
public class FixerIOCurrencyRateProviderBean implements CurrencyRateProvider {

    private static final String URL = "http://api.fixer.io/";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Inject
    private Logger logger;

    @Inject
    private Metadata metadata;

    @Inject
    private Configuration configuration;

    @Override
    public List<CurrencyRate> getRates(Date date, Currency currency, List<Currency> targetCurrencies) throws Exception {
        String dateParam = date != null ? DATE_FORMAT.format(date) : "latest";
        String targetCurrencyCodes = targetCurrencies.stream()
                .map(Currency::getCode)
                .collect(Collectors.joining(","));
        try {
            Map<String, Currency> targetCurrencyMap = targetCurrencies.stream()
                    .collect(Collectors.toMap(Currency::getCode, e -> e));
            HttpResponse<JsonNode> httpResponse = Unirest.get(URL + dateParam)
                    .queryString("access_key", configuration.getConfig(FixerIOConfig.class).getApiKey())
                    .queryString("base", currency.getCode())
                    .queryString("symbols", targetCurrencyCodes)
                    .asJson();
            if (HttpStatus.OK.value() == httpResponse.getStatus()) {
                List<CurrencyRate> currencyRates = new ArrayList<>();
                JSONObject rates = httpResponse.getBody()
                        .getObject()
                        .getJSONObject("rates");
                for (String code : targetCurrencyMap.keySet()) {
                    CurrencyRate currencyRate = metadata.create(CurrencyRate.class);
                    currencyRate.setCurrency(currency);
                    currencyRate.setTargetCurrency(targetCurrencyMap.get(code));
                    currencyRate.setDate(date);
                    currencyRate.setRate(rates.getBigDecimal(code));
                    currencyRates.add(currencyRate);
                }

                return currencyRates;
            } else {
                String errorMessage = String.format("Unable to get fixer.IO currency rate %s/%s for date %s. Error: %s",
                        currency.getCode(), targetCurrencyCodes, dateParam, httpResponse.getBody().toString());
                logger.error(errorMessage);
                throw new IllegalStateException(errorMessage);
            }
        } catch (Exception e) {
            logger.error(String.format("Unable to get fixer.IO currency rate %s/%s for date %s. Error: %s",
                    currency.getCode(), targetCurrencyCodes, dateParam, e.getMessage()), e);
            throw e;
        }
    }
}
