package com.haulmont.addon.currency.web;

import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.web.gui.components.currency_field.CurrencyAddonField;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;
import javax.inject.Inject;

@org.springframework.stereotype.Component(CurrencyFieldRegisterer.NAME)
public class CurrencyFieldRegisterer extends AbstractComponentGenerationStrategy implements Ordered {
    public static final int CURRENCY_FIELD_REGISTERER_ORDER = ComponentGenerationStrategy.HIGHEST_PLATFORM_PRECEDENCE - 5;

    public static final String NAME = "curraddon_CurrencyFieldComponentGeneration";


    @Inject
    public CurrencyFieldRegisterer(Messages messages, ComponentsFactory componentsFactory) {
        super(messages);
        this.componentsFactory = componentsFactory;
    }


    @Override
    public int getOrder() {
        return CURRENCY_FIELD_REGISTERER_ORDER;
    }


    @Nullable
    @Override
    public Component createComponent(ComponentGenerationContext componentGenerationContext) {
        MetaClass metaClass = componentGenerationContext.getMetaClass();
        MetaPropertyPath metaPropertyPath = resolveMetaPropertyPath(metaClass, componentGenerationContext.getProperty());

        Component currencyField = null;
        if (metaPropertyPath != null) {
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
            Class<?> javaType = metaProperty.getJavaType();
            if (CurrencyRateAware.class.isAssignableFrom(javaType)) {
                currencyField = createField(componentGenerationContext);
            }
        }
        return currencyField;
    }


    private Component createField(ComponentGenerationContext componentGenerationContext) {
        Datasource datasource = componentGenerationContext.getDatasource();

        CurrencyField currencyField = componentsFactory.createComponent(CurrencyAddonField.class);
        currencyField.setDatasource(datasource, componentGenerationContext.getProperty());

        return currencyField;
    }
}
