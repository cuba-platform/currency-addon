package com.haulmont.addon.currency.web.gui.xml.layout.loaders;

import com.haulmont.addon.currency.web.gui.components.currency_field.CurrencyAddonField;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractFieldLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Map;

public class CurrencyAddonFieldLoader extends AbstractFieldLoader<CurrencyAddonField> {
    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(CurrencyAddonField.class);
        loadId(resultComponent, element);
    }


    @Override
    public void loadComponent() {
        super.loadComponent();

        loadCurrency(resultComponent, element);
        loadShowCurrencyLabel(resultComponent, element);
        loadCurrencyLabelPosition(resultComponent, element);
        loadDatatype(resultComponent, element);
    }


    protected void loadCurrencyLabelPosition(CurrencyAddonField resultComponent, Element element) {
        String currencyLabelPosition = element.attributeValue("currencyLabelPosition");
        if (StringUtils.isNotEmpty(currencyLabelPosition)) {
            resultComponent.setCurrencyLabelPosition(CurrencyAddonField.CurrencyLabelPosition.valueOf(currencyLabelPosition));
        }
    }


    protected void loadShowCurrencyLabel(CurrencyAddonField resultComponent, Element element) {
        String showCurrency = element.attributeValue("showCurrencyLabel");
        if (StringUtils.isNotEmpty(showCurrency)) {
            resultComponent.setShowCurrencyLabel(Boolean.parseBoolean(showCurrency));
        }
    }


    protected void loadCurrency(CurrencyAddonField resultComponent, Element element) {
        String currency = element.attributeValue("currency");
        if (StringUtils.isNotEmpty(currency)) {
            resultComponent.setCurrency(currency);
        }
    }


    protected void loadDatatype(CurrencyAddonField resultComponent, Element element) {
        if (resultComponent.getDatasource() != null) {
            return;
        }

        String datatype = element.attributeValue("datatype");
        if (StringUtils.isNotEmpty(datatype)) {
            resultComponent.setDatatype(Datatypes.get(datatype));
        }
    }


    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        super.loadDatasource(component, element);

        if (component.getDatasource() == null)
            return;

        Map<String, Object> annotations = component.getMetaPropertyPath().getMetaProperty().getAnnotations();
        Object obj = annotations.get(CurrencyValue.class.getName());
        if (obj == null)
            return;

        //noinspection unchecked
        Map<String, Object> currencyValue = (Map<String, Object>) obj;
        String currencyName = (String) currencyValue.get("currency");
        if (StringUtils.isNotEmpty(currencyName)) {
            ((CurrencyAddonField) component).setCurrency(currencyName);
        }
    }
}
