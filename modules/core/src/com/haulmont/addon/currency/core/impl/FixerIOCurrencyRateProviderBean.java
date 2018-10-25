package com.haulmont.addon.currency.core.impl;

import com.haulmont.addon.currency.core.CurrencyRateProvider;
import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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
    private static final String FIXERIO_SOURCE_NAME = "fixer.io";

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
            String apiKey = configuration.getConfig(FixerIOConfig.class).getApiKey();
            checkApiKey(apiKey);

            HttpResponse<JsonNode> httpResponse = doRequest(currency, dateParam, targetCurrencyCodes, apiKey);
            JsonNode httpResponseBody = httpResponse.getBody();

            LOG.trace("Service response: {}", httpResponseBody);

            checkResponseStatus(httpResponse);

            return parseRates(date, currency, targetCurrencies, httpResponseBody);
        } catch (Exception e) {
            throw new Exception(String.format("Unable to get fixer.IO currency rate %s/%s for date %s",
                    currency.getCode(), targetCurrencyCodes, dateParam), e);
        }
    }


    private void checkResponseStatus(HttpResponse<JsonNode> httpResponse) {
        if (HttpStatus.OK.value() != httpResponse.getStatus()) {
            throw new IllegalStateException("Unable to get fixer.IO currency rate, response code is not 200, " + httpResponse.getBody());
        }
    }


    private HttpResponse<JsonNode> doRequest(CurrencyDescriptor currency, String dateParam, String targetCurrencyCodes, String apiKey) throws UnirestException {
        HttpRequest request = Unirest.get(URL + dateParam)
                .queryString("access_key", apiKey)
                .queryString("base", currency.getCode())
                .queryString("symbols", targetCurrencyCodes);
        return request.asJson();
    }


    private void checkApiKey(String apiKey) throws Exception {
        if (StringUtils.isBlank(apiKey)) {
            throw new Exception("REST API key is empty, please specify it by parameter " + FixerIOConfig.REST_API_KEY_ID);
        }
    }


    private List<CurrencyRate> parseRates(
            Date date, CurrencyDescriptor currency, List<CurrencyDescriptor> targetCurrencies, JsonNode httpResponseBody
    ) throws Exception {
        Map<String, CurrencyDescriptor> targetCurrencyMap = targetCurrencies.stream()
                .collect(Collectors.toMap(CurrencyDescriptor::getCode, e -> e));


        List<CurrencyRate> currencyRates = new ArrayList<>();
        JSONObject responseJsonObject = httpResponseBody
                .getObject();
        checkSuccess(responseJsonObject);

        JSONObject rates = responseJsonObject.getJSONObject("rates");
        for (String code : targetCurrencyMap.keySet()) {
            CurrencyRate currencyRate = metadata.create(CurrencyRate.class);
            currencyRate.setSource(FIXERIO_SOURCE_NAME);
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
