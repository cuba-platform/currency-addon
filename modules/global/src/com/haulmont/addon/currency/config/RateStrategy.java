package com.haulmont.addon.currency.config;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import static com.haulmont.addon.currency.CurrencyGlobalConstants.RateStrategyConstants;

public enum RateStrategy implements EnumClass<String> {
    /**
     * Show error, if rate for current date not found
     */
    REQUIRED(RateStrategyConstants.REQUIRED),

    /**
     * Show by last available rate and show warning about absence rate for valid period
     */
    WARNING(RateStrategyConstants.WARNING),

    /**
     * Show by last available rate date
     */
    LAST_DATE(RateStrategyConstants.LAST_AVAILABLE);



    private final String id;



    RateStrategy(String id) {
        this.id = id;
    }



    @Override
    public String getId() {
        return id;
    }


    //Used by CUBA to load enum configuration property
    public static RateStrategy fromId(String id) {
        RateStrategy result = null;
        for (RateStrategy redundancy : values()) {
            if (redundancy.getId().equals(id)) {
                result = redundancy;
                break;
            }
        }
        return result;
    }
}
