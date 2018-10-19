package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators;

import com.haulmont.addon.currency.config.CurrencyConfig;
import com.haulmont.addon.currency.config.RateStrategy;
import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.format.CurrencyBigDecimalFormat;
import com.haulmont.addon.currency.service.ConvertResult;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyValueDataProvider;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.VBoxLayout;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class WriteApplicablePopupProvider extends AbstractCurrencyButtonPopupContentProvider {

    protected static final String CURRENCY_FIELD_NAMESPACE = "com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield";

    protected final FormatStringsRegistry formatStringsRegistry = AppBeans.get(FormatStringsRegistry.class);
    protected final UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
    protected final CurrencyBigDecimalFormat currencyFormat = AppBeans.get(CurrencyBigDecimalFormat.class);
    protected final CurrencyConfig config = AppBeans.get(Configuration.class).getConfig(CurrencyConfig.class);
    protected final Messages messages = AppBeans.get(Messages.class);

    protected final SimpleDateFormat rateDateFormat;


    public WriteApplicablePopupProvider(
            CurrencyValueDataProvider dataProvider, boolean withTime
    ) {
        super(dataProvider, withTime);

        if (config.getShowUsedConversionRateDate()) {
            String dateTimeFormat = formatStringsRegistry.getFormatStrings(userSessionSource.getLocale()).getDateTimeFormat();
            rateDateFormat = new SimpleDateFormat(dateTimeFormat);
        } else {
            rateDateFormat = null;
        }
    }


    @Override
    protected DateField createDateField() {
        DateField dateField = componentsFactory.createComponent(DateField.class);
        dateField.addValueChangeListener(e -> dataProvider.setDate((Date) e.getValue()));
        dateField.setResolution(isWithTime() ? DateField.Resolution.MIN : DateField.Resolution.DAY);
        return dateField;
    }


    protected static class NewCurrencyValue {
        protected CurrencyDescriptor currency;
        protected BigDecimal value;


        public NewCurrencyValue(CurrencyDescriptor currency, BigDecimal value) {
            this.currency = currency;
            this.value = value;
        }


        public CurrencyDescriptor getCurrency() {
            return currency;
        }


        public BigDecimal getValue() {
            return value;
        }
    }


    @Override
    protected void addCurrencyControls(VBoxLayout layout, Date amountDate) {
        Map<String, NewCurrencyValue> options = createOptions(amountDate);

        if (options.isEmpty()) {
            Label noRatesLabel = componentsFactory.createComponent(Label.class);
            String message = messages.getMessage(CURRENCY_FIELD_NAMESPACE, "rates_not_found");
            noRatesLabel.setValue(message);

            layout.add(noRatesLabel);
        } else {
            OptionsGroup optionsGroup = createOptionsGroup(options);
            layout.add(optionsGroup);
        }
    }


    private OptionsGroup createOptionsGroup(Map<String, NewCurrencyValue> options) {
        OptionsGroup optionsGroup = componentsFactory.createComponent(OptionsGroup.class);
        optionsGroup.setOptionsMap(options);

        optionsGroup.addValueChangeListener(e -> {
            NewCurrencyValue newCurrencyValue = (NewCurrencyValue) e.getValue();

            dataProvider.setCurrency(newCurrencyValue.getCurrency());
            dataProvider.setAmount(newCurrencyValue.getValue());
        });
        return optionsGroup;
    }


    private Map<String, NewCurrencyValue> createOptions(Date amountDate) {
        CurrencyDescriptor oldCurrency = dataProvider.getCurrency();

        List<CurrencyDescriptor> currencies = currencyService.getAvailableCurrencies();
        Map<String, NewCurrencyValue> options = new HashMap<>();
        for (CurrencyDescriptor targetCurrency : currencies) {
            if (targetCurrency != null && !targetCurrency.equals(oldCurrency)) {
                addCurrencyOption(amountDate, options, targetCurrency);
            }
        }
        return options;
    }


    private void addCurrencyOption(Date amountDate, Map<String, NewCurrencyValue> options, CurrencyDescriptor targetCurrency) {
        CurrencyDescriptor oldCurrency = dataProvider.getCurrency();
        BigDecimal oldAmount = dataProvider.getAmount();
        if (oldAmount == null) {
            oldAmount = BigDecimal.ZERO;
        }

        ConvertResult convertResult = currencyService.convertAmountToRate(oldAmount, amountDate, oldCurrency, targetCurrency);

        String labelPattern = "";
        List<String> patternParams = new ArrayList<>();

        if (convertResult != null) {
            BigDecimal newAmount = convertResult.getResultAmount();

            String formattedNewAmount = currencyFormat.format(newAmount, targetCurrency.getPrecision());
            labelPattern += "%s" ;
            patternParams.add(formattedNewAmount);

            labelPattern += " %s";
            patternParams.add(targetCurrency.getSymbol());

            labelPattern = addRateDateToLabel(convertResult, labelPattern, patternParams);

            NewCurrencyValue optionValue = new NewCurrencyValue(targetCurrency, newAmount);
            long secondsDifference = amountDate.toInstant().getEpochSecond() - convertResult.getUsedRate().getDate().toInstant().getEpochSecond();

            formatAndAddOption(options, labelPattern, patternParams, optionValue, secondsDifference);
        }
    }


    private void formatAndAddOption(
            Map<String, NewCurrencyValue> options,
            String labelPattern,
            List<String> patternParams,
            NewCurrencyValue optionValue,
            long secondsDifference
    ) {
        RateStrategy rateStrategy = config.getRateStrategy();
        boolean rateToOld = secondsDifference > config.getRateActualPeriodSeconds();

        if (rateStrategy == RateStrategy.REQUIRED) {
            if (!rateToOld) {
                addOptionToMap(options, labelPattern, patternParams, optionValue);
            }
        } else if (rateStrategy == RateStrategy.WARNING) {
            if (rateToOld) {
                labelPattern += " %s";
                patternParams.add(messages.getMessage(CURRENCY_FIELD_NAMESPACE, "to_old"));
            }
            addOptionToMap(options, labelPattern, patternParams, optionValue);
        } else if (rateStrategy == RateStrategy.LAST_DATE) {
            addOptionToMap(options, labelPattern, patternParams, optionValue);
        } else {
            throw new DevelopmentException("Unknown rate strategy: " + rateStrategy);
        }
    }


    private String addRateDateToLabel(ConvertResult convertResult, String labelPattern, List<String> patternParams) {
        if (rateDateFormat != null) {
            labelPattern += " (%s)";
            String formattedRateDate = rateDateFormat.format(convertResult.getUsedRate().getDate());
            patternParams.add(formattedRateDate);
        }
        return labelPattern;
    }


    private void addOptionToMap(
            Map<String, NewCurrencyValue> options,
            String labelPattern,
            List<String> patternParams,
            NewCurrencyValue optionValue
    ) {
        String changeCurrencyLabel = String.format(labelPattern, patternParams.toArray());
        options.put(changeCurrencyLabel, optionValue);
    }

}
