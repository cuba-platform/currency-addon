package com.haulmont.addon.currency.web.currencyrate;

import com.haulmont.addon.currency.service.CurrencyRatesService;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.CheckBox;

import javax.inject.Inject;
import java.util.Map;

public class CurrencyRateBrowse extends AbstractLookup {

    @Inject
    private CheckBox rateUpdateSchedulingEnabled;
    @Inject
    private CurrencyRatesService currencyRatesService;

    @Override
    public void init(Map<String, Object> params) {
        Boolean active = currencyRatesService.autoUpdateIsActive();

        rateUpdateSchedulingEnabled.setValue(active);

        rateUpdateSchedulingEnabled.addValueChangeListener(valueChangeEvent -> {
            boolean isActive = (boolean) valueChangeEvent.getValue();
            currencyRatesService.setAutoUpdateActive(isActive);
        });
    }



}