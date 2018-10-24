package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractDatasource;

import java.math.BigDecimal;
import java.util.Date;

public class DataSourceCurrencyProvider implements CurrencyDataProvider {

    private final Metadata metadata = AppBeans.get(Metadata.class);
    private final TimeSource timeSource = AppBeans.get(TimeSource.class);
    private final CurrencyService currencyService = AppBeans.get(CurrencyService.class);

    private final Datasource dataSource;
    private final String entityReferencePropertyName;
    private final CurrencyValueChangedEventSupplier changeEventSupplier;


    public DataSourceCurrencyProvider(
            Datasource dataSource, String entityReferencePropertyName, CurrencyValueChangedEventSupplier changeEventSupplier
    ) {
        this.dataSource = dataSource;
        this.entityReferencePropertyName = entityReferencePropertyName;
        this.changeEventSupplier = changeEventSupplier;

        dataSource.addItemChangeListener(this::setCurrencyDescriptorOnLoadDataSource);

        //On load item to DS
        dataSource.addItemChangeListener(event -> changeEventSupplier.modelChanged());

        DsContext dsContext = dataSource.getDsContext();
        dsContext.addBeforeCommitListener(this::setCurrentDateIfNull);
        dsContext.addBeforeCommitListener(this::addCurrencyEntityToCommit);
    }


    protected void setCurrentDateIfNull(CommitContext context) {
        if (getItem().getDate() == null) {
            getItem().setDate(timeSource.currentTimestamp());
        }
    }


    protected void addCurrencyEntityToCommit(CommitContext context) {
        CurrencyRateAware currencyRateAware = getItem();
        if (currencyRateAware instanceof Entity) {
            context.addInstanceToCommit((Entity) currencyRateAware);
        }
    }


    protected void setCurrencyDescriptorOnLoadDataSource(Datasource.ItemChangeEvent event) {
        Entity item = event.getItem();
        CurrencyRateAware addonCurrencyField = item.getValue(entityReferencePropertyName);

        CurrencyDescriptor currencyDescriptor = null;
        if (addonCurrencyField != null) {
            currencyDescriptor = addonCurrencyField.getCurrency();
        }

        if (currencyDescriptor == null) {
            currencyDescriptor = currencyService.getDefaultCurrency();
            setCurrency(currencyDescriptor);
        }
    }


    @Override
    public CurrencyRateAware getItem() {
        Entity item = dataSource.getItem();
        return item != null ? item.getValue(entityReferencePropertyName) : null;
    }


    @Override
    public void setItem(CurrencyRateAware currencyRateAware) {
        Entity item = dataSource.getItem();
        if (item != null) {
            item.setValue(entityReferencePropertyName, currencyRateAware);
        }
    }


    private CurrencyRateAware getOrCreateEntity() {
        Entity item = dataSource.getItem();

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
        MetaProperty currencyFieldMetaProperty = dataSource.getMetaClass().getPropertyNN(entityReferencePropertyName);
        currencyRateAware = (CurrencyRateAware) metadata.create(currencyFieldMetaProperty.getJavaType());
        currencyRateAware.setDate(new Date());
        return currencyRateAware;
    }


    // In case when modified only Currency value data source not marked as changed so we need do it manually
    private void markDataSourceAsModified() {
        AbstractDatasource abstractDatasource = (AbstractDatasource) dataSource;
        abstractDatasource.setModified(true);
    }


    @Override
    public Date getDate() {
        CurrencyRateAware entity = getItem();
        return entity != null ? entity.getDate() : null;
    }


    @Override
    public CurrencyDescriptor getCurrency() {
        CurrencyRateAware entity = getItem();
        return entity != null ? entity.getCurrency() : null;
    }


    @Override
    public BigDecimal getAmount() {
        CurrencyRateAware entity = getItem();
        return entity != null ? entity.getValue() : null;
    }


    @Override
    public void setCurrency(CurrencyDescriptor newCurrency) {
        CurrencyRateAware entity = getOrCreateEntity();
        if (entity != null) {
            entity.setCurrency(newCurrency);
        }
        changeEventSupplier.modelChanged();
    }


    @Override
    public void setAmount(BigDecimal newAmount) {
        CurrencyRateAware entity = getOrCreateEntity();
        if (entity != null) {
            entity.setValue(newAmount);
        }
        changeEventSupplier.modelChanged();
    }


    @Override
    public void setDate(Date value) {
        CurrencyRateAware entity = getOrCreateEntity();
        if (entity != null) {
            entity.setDate(value);
        }
        changeEventSupplier.modelChanged();
    }
}
