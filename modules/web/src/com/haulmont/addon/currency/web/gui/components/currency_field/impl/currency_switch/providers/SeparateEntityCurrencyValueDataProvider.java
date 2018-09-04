package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.AddonCurrencyValue;
import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyValueEntity;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;

import java.math.BigDecimal;
import java.util.Date;

public class SeparateEntityCurrencyValueDataProvider implements CurrencyValueDataProvider {

    private final Metadata metadata = AppBeans.get(Metadata.class);

    private final Datasource datasource;
    private final String entityReferencePropertyName;
    private final CurrencyValueChangedEventSupplier changeEventSupplier;


    public SeparateEntityCurrencyValueDataProvider(
            Datasource datasource, String entityReferencePropertyName, CurrencyValueChangedEventSupplier changeEventSupplier
    ) {
        this.datasource = datasource;
        this.entityReferencePropertyName = entityReferencePropertyName;
        this.changeEventSupplier = changeEventSupplier;

        DsContext dsContext = datasource.getDsContext();
        dsContext.addBeforeCommitListener(context -> {
            if (getEntity().getDate() == null) {
                getEntity().setDate(new Date());
            }
        });
        dsContext.addBeforeCommitListener(context -> context.addInstanceToCommit(getEntity()));
    }


    public Datasource getDatasource() {
        return datasource;
    }


    private AddonCurrencyValue getEntity() {
        Entity item = datasource.getItem();

        return item != null ? item.getValue(entityReferencePropertyName) : null;
    }

    private AddonCurrencyValue getOrCreateEntity() {
        Entity item = datasource.getItem();

        AddonCurrencyValue value = null;
        if (item != null) {
            value = item.getValue(entityReferencePropertyName);
            if (value == null) {
                value = metadata.create(CurrencyValueEntity.class);
                item.setValue(entityReferencePropertyName, value);
            }
        }
        return value;
    }


    @Override
    public Date getDate() {
        AddonCurrencyValue entity = getEntity();
        return entity != null ? entity.getDate() : null;
    }


    @Override
    public Currency getCurrency() {
        AddonCurrencyValue entity = getEntity();
        return entity != null ? entity.getCurrency() : null;
    }


    @Override
    public BigDecimal getAmount() {
        AddonCurrencyValue entity = getEntity();
        return entity != null ? entity.getValue() : null;
    }


    @Override
    public void setCurrency(Currency newCurrency) {
        AddonCurrencyValue entity = getOrCreateEntity();
        if (entity != null) {
            entity.setCurrency(newCurrency);
        }
        changeEventSupplier.updatePopupContent();
    }


    public void setAmount(BigDecimal newAmount) {
        AddonCurrencyValue entity = getOrCreateEntity();
        if (entity != null) {
            entity.setValue(newAmount);
        }
        changeEventSupplier.updatePopupContent();
    }


    public void setDate(Date value) {
        AddonCurrencyValue entity = getOrCreateEntity();
        if (entity != null) {
            entity.setDate(value);
        }
        changeEventSupplier.updatePopupContent();
    }
}
