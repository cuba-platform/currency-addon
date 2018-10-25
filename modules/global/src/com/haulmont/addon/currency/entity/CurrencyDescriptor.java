package com.haulmont.addon.currency.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Description of currency from real world
 */
@NamePattern("%s|name")
@Table(name = "CURRADDON_CURRENCY_DESCRIPTOR")
@Entity(name = "curraddon$CurrencyDescriptor")
public class CurrencyDescriptor extends StandardEntity {
    private static final long serialVersionUID = 9158629870540811438L;

    /**
     * Code of currency, e.g. USD, EUR, RUB, ...
     */
    @NotNull
    @Column(name = "CODE", nullable = false, unique = true, length = 20)
    protected String code;


    /**
     * Should be currency active for exchange and select
     */
    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    protected Boolean active = false;


    /**
     * Symbols near to amount e.g. $, €, руб., ...
     */
    @Column(name = "SYMBOL", length = 4)
    protected String symbol;


    /**
     * Name of currency
     * E.g. "US Dollar", "Euro", "Russian Ruble", ...
     */
    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;


    /**
     * Count of digits after fraction separator
     */
    @NumberFormat(pattern = "##")
    @Max(19)
    @Min(0)
    @NotNull
    @Column(name = "PRECISION_", nullable = false)
    protected Integer precision;

    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "currency")
    protected List<CurrencyRate> rates;



    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getPrecision() {
        return precision;
    }





    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }


    public void setRates(List<CurrencyRate> rates) {
        this.rates = rates;
    }

    public List<CurrencyRate> getRates() {
        return rates;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}