package com.haulmont.addon.currency.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@NamePattern("%s %s|value,currency")
@Table(name = "CURRADDON_CURRENCY")
@Entity(name = "curraddon$Currency")
public class Currency extends StandardEntity implements CurrencyRateAware {
    private static final long serialVersionUID = 8530608597300769485L;

    @NotNull
    @Column(name = "VALUE_", nullable = false, precision = 19, scale = 12)
    protected BigDecimal value;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "DATE_", nullable = false)
    protected Date date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_ID")
    protected CurrencyDescriptor currency;
    public CurrencyDescriptor getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDescriptor currency) {
        this.currency = currency;
    }



    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }



    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }


}