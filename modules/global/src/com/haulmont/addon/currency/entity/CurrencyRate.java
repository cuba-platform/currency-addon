package com.haulmont.addon.currency.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.ManyToOne;

@NamePattern("%s|date")
@Table(name = "CURRADDON_CURRENCY_RATE")
@Entity(name = "curraddon$CurrencyRate")
public class CurrencyRate extends StandardEntity {
    private static final long serialVersionUID = 6258974533631716401L;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "DATE_", nullable = false)
    protected Date date;

    @NotNull
    @Column(name = "RATE", nullable = false, precision = 19, scale = 6)
    protected BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_ID")
    protected CurrencyDescriptor currency;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TARGET_CURRENCY_ID")
    protected CurrencyDescriptor targetCurrency;

    public CurrencyDescriptor getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDescriptor currency) {
        this.currency = currency;
    }

    public CurrencyDescriptor getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyDescriptor targetCurrency) {
        this.targetCurrency = targetCurrency;
    }






    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }




}