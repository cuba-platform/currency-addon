package com.haulmont.addon.currency.web.gui.xml.layout.loaders;

import com.haulmont.addon.currency.web.CurrencyWebConstants;
import com.haulmont.addon.currency.web.gui.components.currency_field.CurrencyAddonField;
import com.haulmont.addon.currency.web.gui.components.currency_field.Side;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractFieldLoader;
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

        loadFlagWithTime(resultComponent, element);

        loadCurrencyButtonPositionSide(resultComponent, element);
    }


    protected void loadFlagWithTime(CurrencyAddonField resultComponent, Element element) {
        String attributeValue = element.attributeValue(CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_XML_ATTR_NAME);
        boolean withTime = CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_DEFAULT;
        if (attributeValue != null) {
            withTime = Boolean.getBoolean(attributeValue);
        }
        resultComponent.setWithTime(withTime);
    }


    private void loadCurrencyButtonPositionSide(CurrencyAddonField resultComponent, Element element) {
        String currencyButtonPositionAttrValue = element.attributeValue(CurrencyWebConstants.CURRENCY_BUTTON_POSITION_XML_ATTR_NAME);
        Side currencyButtonSide = CurrencyWebConstants.CURRENCY_BUTTON_POSITION_DEFAULT;
        if (currencyButtonPositionAttrValue != null) {
            currencyButtonSide = Side.byValue(currencyButtonPositionAttrValue);
        }
        resultComponent.setCurrencyButtonPosition(currencyButtonSide);
    }




    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        super.loadDatasource(component, element);
    }

}
