package com.haulmont.addon.currency.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.List;
import javax.persistence.OneToMany;

@NamePattern("%s|name")
@Table(name = "CURRADDON_CURRENCY")
@Entity(name = "curraddon$Currency")
public class Currency extends StandardEntity {
    private static final long serialVersionUID = 9158629870540811438L;

    @NotNull
    @Column(name = "CODE", nullable = false, unique = true, length = 20)
    protected String code;

    @Column(name = "SYMBOL", length = 4)
    protected String symbol;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "currency")
    protected List<CurrencyRate> rates;

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