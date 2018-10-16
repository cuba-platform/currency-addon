package com.haulmont.addon.currency.config;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum RateRedundancy implements EnumClass<String> {
    /**
     * Show error, if rate for current date not found
     */
    SAME_DATE_REQUIRED("SAME_DATE_REQUIRED"),

    /**
     * Show by last available rate and show warning about absence rate for current day
     */
    SAME_DATE_WARNING("SAME_DATE_WARNING"),

    /**
     * Show by last available rate date
     */
    LAST_DATE("LAST_DATE");

    private final String id;



    RateRedundancy(String id) {
        this.id = id;
    }


    @Override
    public String getId() {
        return id;
    }

    public static RateRedundancy fromId(String id) {
        RateRedundancy result = null;
        for (RateRedundancy redundancy : values()) {
            if (redundancy.getId().equals(id)) {
                result = redundancy;
                break;
            }
        }
        return result;
    }
}
