package com.haulmont.addon.currency.web.gui.components.currency_field;

import com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition;

/**
 * Generic constant for left/right values
 */
public enum Side {
    Left("LEFT", CurrencyLabelPosition.LEFT),
    Right("RIGHT", CurrencyLabelPosition.RIGHT);

    private final String value;
    private final CurrencyLabelPosition currencyLabelPosition;


    Side(String value, CurrencyLabelPosition currencyLabelPosition) {
        this.value = value;
        this.currencyLabelPosition = currencyLabelPosition;
    }


    public String getValue() {
        return value;
    }


    public CurrencyLabelPosition getCurrencyLabelPosition() {
        return currencyLabelPosition;
    }


    public static Side byValue(String value) {
        for (Side side : values()) {
            if (side.value.equals(value)) {
                return side;
            }
        }
        return null;
    }


    public static Side byCurrencyLabelPosition(CurrencyLabelPosition currencyLabelPosition) {
        for (Side side : values()) {
            if (side.currencyLabelPosition == currencyLabelPosition) {
                return side;
            }
        }
        return null;
    }

}
