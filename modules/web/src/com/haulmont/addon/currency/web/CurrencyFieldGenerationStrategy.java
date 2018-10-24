package com.haulmont.addon.currency.web;

import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.web.gui.components.currency_field.CurrencyAddonField;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractComponentGenerationStrategy;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentGenerationContext;
import com.haulmont.cuba.gui.components.ComponentGenerationStrategy;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Order(CurrencyFieldGenerationStrategy.CURRENCY_FIELD_REGISTERER_ORDER)
@org.springframework.stereotype.Component(CurrencyFieldGenerationStrategy.NAME)
public class CurrencyFieldGenerationStrategy extends AbstractComponentGenerationStrategy {

    public static final int CURRENCY_FIELD_REGISTERER_ORDER = ComponentGenerationStrategy.HIGHEST_PLATFORM_PRECEDENCE - 5;

    public static final String NAME = "curraddon_CurrencyFieldComponentGeneration";


    @Inject
    public CurrencyFieldGenerationStrategy(Messages messages, ComponentsFactory componentsFactory) {
        super(messages);
        this.componentsFactory = componentsFactory;
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


    protected Component createField(ComponentGenerationContext componentGenerationContext) {
        CurrencyAddonField currencyField = componentsFactory.createComponent(CurrencyAddonField.class);

        configureParameterWithTime(componentGenerationContext, currencyField);

        configureDataSource(componentGenerationContext, currencyField);

        return currencyField;
    }


    private void configureDataSource(ComponentGenerationContext componentGenerationContext, CurrencyAddonField currencyField) {
        Datasource datasource = componentGenerationContext.getDatasource();
        currencyField.setDatasource(datasource, componentGenerationContext.getProperty());
    }


    protected void configureParameterWithTime(ComponentGenerationContext componentGenerationContext, CurrencyAddonField currencyField) {
        String withTimeString = componentGenerationContext.getXmlDescriptor()
                .attributeValue(CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_XML_ATTR_NAME);
        boolean withTime = CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_DEFAULT;
        if (withTimeString != null) {
            withTime =  Boolean.parseBoolean(withTimeString);
        }
        currencyField.setWithTime(withTime);
    }
}
