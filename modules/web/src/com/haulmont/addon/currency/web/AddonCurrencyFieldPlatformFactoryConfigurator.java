package com.haulmont.addon.currency.web;

import com.haulmont.addon.currency.web.gui.components.currency_field.impl.WebCurrencyAddonField;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AddonCurrencyFieldPlatformFactoryConfigurator {

    @PostConstruct
    public void replaceCurrencyFieldImplementation() throws NoSuchFieldException, IllegalAccessException {
        WebComponentsFactory.registerComponent(CurrencyField.NAME, WebCurrencyAddonField.class);
    }

}
