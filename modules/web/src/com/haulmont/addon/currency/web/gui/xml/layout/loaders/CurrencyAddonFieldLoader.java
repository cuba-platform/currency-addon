package com.haulmont.addon.currency.web.gui.xml.layout.loaders;

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
    }


    protected void loadCurrencyLabelPosition(CurrencyAddonField resultComponent, Element element) {
        String currencyLabelPosition = element.attributeValue("currencyLabelPosition");
        if (StringUtils.isNotEmpty(currencyLabelPosition)) {
            resultComponent.setCurrencyLabelPosition(CurrencyField.CurrencyLabelPosition.valueOf(currencyLabelPosition));
        }
    }


    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        super.loadDatasource(component, element);
    }

}
