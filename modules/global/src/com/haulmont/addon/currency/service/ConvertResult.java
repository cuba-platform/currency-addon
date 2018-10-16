package com.haulmont.addon.currency.service;

import com.haulmont.addon.currency.entity.CurrencyRate;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConvertResult implements Serializable {
    private CurrencyRate usedRate;
    private BigDecimal resultAmount;


    public ConvertResult(CurrencyRate usedRate, BigDecimal resultAmount) {
        this.usedRate = usedRate;
        this.resultAmount = resultAmount;
    }


    public CurrencyRate getUsedRate() {
        return usedRate;
    }


    public BigDecimal getResultAmount() {
        return resultAmount;
    }
}
