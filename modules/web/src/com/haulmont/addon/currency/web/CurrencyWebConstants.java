package com.haulmont.addon.currency.web;

import com.haulmont.cuba.gui.components.CurrencyField;

public interface CurrencyWebConstants {
    String CURRENCY_DATE_WITH_TIME_XML_ATTR_NAME = "withTime";
    boolean CURRENCY_DATE_WITH_TIME_DEFAULT = false;

    String CURRENCY_BUTTON_POSITION_XML_ATTR_NAME = "currencyButtonPosition";
    CurrencyField.CurrencyLabelPosition CURRENCY_BUTTON_POSITION_DEFAULT = CurrencyField.CurrencyLabelPosition.RIGHT;

    interface Css {
        String FIELD_CLASS = "c-currencyfield";
        String FIELD_LAYOUT = "c-currencyfield-layout";
        String FIELD_TEXT = "c-currencyfield-text";
        String FIELD_CURRENCY = "c-currencyfield-currency";
    }
}
