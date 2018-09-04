package com.haulmont.addon.currency.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@NamePattern("%s %s|value,currency")
@Table(name = "CURRADDON_CURRENCY_VALUE")
@Entity(name = "curraddon$CurrencyValueEntity")
public class CurrencyValueEntity extends StandardEntity implements AddonCurrencyValue {
    private static final long serialVersionUID = 8530608597300769485L;

    @NotNull
    @Column(name = "VALUE_", nullable = false)
    protected BigDecimal value;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "DATE_", nullable = false)
    protected Date date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_ID")
    protected Currency currency;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }


}