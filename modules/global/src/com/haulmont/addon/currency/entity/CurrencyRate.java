package com.haulmont.addon.currency.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Exchange rate for transfer from <strong>currency</strong> to <strong>targetCurrency</strong>
 */
@NamePattern("%s|date")
@Table(name = "CURRADDON_CURRENCY_RATE")
@Entity(name = "curraddon$CurrencyRate")
public class CurrencyRate extends StandardEntity {
    private static final long serialVersionUID = 6258974533631716401L;


    /**
     * Date when rate is actual
     */
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "DATE_", nullable = false)
    protected Date date;


    /**
     * Multiplicator of exchange from <strong>currency</strong> to <strong>targetCurrency</strong>
     */
    @NotNull
    @Column(name = "RATE", nullable = false, precision = 19, scale = 12)
    protected BigDecimal rate;


    /**
     * Currency for converting from
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_ID")
    protected CurrencyDescriptor currency;


    /**
     * Currency for converting to
     */
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TARGET_CURRENCY_ID")
    protected CurrencyDescriptor targetCurrency;


    /**
     * Source of rate.
     * It can be web service name or username who created rate or something else.
     */
    @NotNull
    @Column(name = "SOURCE", length = 100)
    protected String source;


    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }


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