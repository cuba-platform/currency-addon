package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractDatasource;

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


    private CurrencyRateAware getEntity() {
        Entity item = datasource.getItem();

        return item != null ? item.getValue(entityReferencePropertyName) : null;
    }


    private CurrencyRateAware getOrCreateEntity() {
        Entity item = datasource.getItem();

        markDataSourceAsModified();

        CurrencyRateAware currencyRateAware = null;
        if (item != null) {
            currencyRateAware = item.getValue(entityReferencePropertyName);
            if (currencyRateAware == null) {
                currencyRateAware = createCurrencyRateAware();
                item.setValue(entityReferencePropertyName, currencyRateAware);
            }
        }
        return currencyRateAware;
    }


    private CurrencyRateAware createCurrencyRateAware() {
        CurrencyRateAware currencyRateAware;
        MetaProperty currencyFieldMetaProperty = datasource.getMetaClass().getPropertyNN(entityReferencePropertyName);
        currencyRateAware = (CurrencyRateAware) metadata.create(currencyFieldMetaProperty.getJavaType());
        currencyRateAware.setDate(new Date());
        return currencyRateAware;
    }


    // In case when modified only Currency value data source not marked as changed so we need do it manually
    private void markDataSourceAsModified() {
        AbstractDatasource abstractDatasource = (AbstractDatasource) datasource;
        abstractDatasource.setModified(true);
    }


    @Override
    public Date getDate() {
        CurrencyRateAware entity = getEntity();
        return entity != null ? entity.getDate() : null;
    }


    @Override
    public CurrencyDescriptor getCurrency() {
        CurrencyRateAware entity = getEntity();
        return entity != null ? entity.getCurrency() : null;
    }


    @Override
    public BigDecimal getAmount() {
        CurrencyRateAware entity = getEntity();
        return entity != null ? entity.getValue() : null;
    }


    @Override
    public void setCurrency(CurrencyDescriptor newCurrency) {
        CurrencyRateAware entity = getOrCreateEntity();
        if (entity != null) {
            entity.setCurrency(newCurrency);
        }
        changeEventSupplier.updatePopupContent();
    }


    public void setAmount(BigDecimal newAmount) {
        CurrencyRateAware entity = getOrCreateEntity();
        if (entity != null) {
            entity.setValue(newAmount);
        }
        changeEventSupplier.updatePopupContent();
    }


    public void setDate(Date value) {
        CurrencyRateAware entity = getOrCreateEntity();
        if (entity != null) {
            entity.setDate(value);
        }
        changeEventSupplier.updatePopupContent();
    }
}
