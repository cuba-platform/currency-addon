package com.haulmont.addon.currency.core.impl;

import com.haulmont.addon.currency.core.CurrencyRateProvider;
import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(FixerIOCurrencyRateProviderBean.class);

    private static final String URL = "http://data.fixer.io/api/";

    private static final SimpleDateFormat FIXERIO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Inject
    private Metadata metadata;

    @Inject
    private Configuration configuration;

    @Override
    public List<CurrencyRate> getRates(Date date, CurrencyDescriptor currency, List<CurrencyDescriptor> targetCurrencies) throws Exception {
        String dateParam = date != null ? FIXERIO_DATE_FORMAT.format(date) : "latest";
        String targetCurrencyCodes = targetCurrencies.stream()
                .map(CurrencyDescriptor::getCode)
                .collect(Collectors.joining(","));
        try {
            Map<String, CurrencyDescriptor> targetCurrencyMap = targetCurrencies.stream()
                    .collect(Collectors.toMap(CurrencyDescriptor::getCode, e -> e));
            String apiKey = configuration.getConfig(FixerIOConfig.class).getApiKey();
            if (StringUtils.isBlank(apiKey)) {
                throw new Exception("REST API key is empty, please specify it by parameter " + FixerIOConfig.REST_API_KEY_ID);
            }

            HttpRequest request = Unirest.get(URL + dateParam)
                    .queryString("access_key", apiKey)
                    .queryString("base", currency.getCode())
                    .queryString("symbols", targetCurrencyCodes);
            HttpResponse<JsonNode> httpResponse = request.asJson();

            if (HttpStatus.OK.value() == httpResponse.getStatus()) {
                return parseRates(date, currency, targetCurrencyMap, httpResponse);
            } else {
                throw new IllegalStateException("Unable to get fixer.IO currency rate, response code is not 200, " + httpResponse.getBody());
            }
        } catch (Exception e) {
            throw new Exception(String.format("Unable to get fixer.IO currency rate %s/%s for date %s",
                    currency.getCode(), targetCurrencyCodes, dateParam), e);
        }
    }


    private List<CurrencyRate> parseRates(
            Date date, CurrencyDescriptor currency, Map<String, CurrencyDescriptor> targetCurrencyMap, HttpResponse<JsonNode> httpResponse
    ) throws Exception {
        LOG.trace("Service response: {}", httpResponse.getBody());
        List<CurrencyRate> currencyRates = new ArrayList<>();
        JSONObject responseJsonObject = httpResponse.getBody()
                .getObject();
        checkSuccess(responseJsonObject);

        JSONObject rates = responseJsonObject.getJSONObject("rates");
        for (String code : targetCurrencyMap.keySet()) {
            CurrencyRate currencyRate = metadata.create(CurrencyRate.class);
            currencyRate.setCurrency(currency);
            currencyRate.setTargetCurrency(targetCurrencyMap.get(code));
            currencyRate.setDate(date);
            currencyRate.setRate(rates.getBigDecimal(code));
            currencyRates.add(currencyRate);
        }

        return currencyRates;
    }

    private void checkSuccess(JSONObject responseJsonObject) throws Exception {
        boolean success = responseJsonObject.getBoolean("success");
        if (!success) {
            JSONObject error = responseJsonObject.getJSONObject("error");
            throw new Exception("Service error: " + error);
        }
    }
}
