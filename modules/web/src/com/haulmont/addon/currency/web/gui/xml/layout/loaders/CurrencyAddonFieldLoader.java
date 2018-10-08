package com.haulmont.addon.currency.web.gui.xml.layout.loaders;

import com.haulmont.addon.currency.web.CurrencyWebConstants;
import com.haulmont.addon.currency.web.gui.components.currency_field.CurrencyAddonField;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractFieldLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CurrencyAddonFieldLoader extends AbstractFieldLoader<CurrencyAddonField> {

    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(CurrencyAddonField.class);
        loadId(resultComponent, element);
    }


    @Override
    public void loadComponent() {
        super.loadComponent();

        loadCurrencyLabelPosition(resultComponent, element);
        loadFlagWithTime(resultComponent, element);
    }


    protected void loadFlagWithTime(CurrencyAddonField resultComponent, Element element) {
        String attributeValue = element.attributeValue(CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_XML_ATTR_NAME);
        boolean withTime = CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_DEFAULT;
        if (attributeValue != null) {
            withTime = Boolean.getBoolean(attributeValue);
        }
        resultComponent.setWithTime(withTime);
    }


    protected void loadCurrencyLabelPosition(CurrencyAddonField resultComponent, Element element) {
        String currencyButtonPosition = element.attributeValue(CurrencyWebConstants.CURRENCY_BUTTON_POSITION_XML_ATTR_NAME);
        CurrencyField.CurrencyLabelPosition position = CurrencyWebConstants.CURRENCY_BUTTON_POSITION_DEFAULT;
        if (StringUtils.isNotEmpty(currencyButtonPosition)) {
            position = CurrencyField.CurrencyLabelPosition.valueOf(currencyButtonPosition);
        }
        resultComponent.setCurrencyButtonPosition(position);
    }


    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        super.loadDatasource(component, element);
    }

}
