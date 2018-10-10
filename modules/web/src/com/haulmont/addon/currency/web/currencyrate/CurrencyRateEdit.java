package com.haulmont.addon.currency.web.currencyrate;

import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

public class CurrencyRateEdit extends AbstractEditor<CurrencyRate> {

    @Inject
    private UserSession userSession;

    @Override
    protected void initNewItem(CurrencyRate item) {
        User currentUser = userSession.getCurrentOrSubstitutedUser();
        item.setSource(currentUser.getName());
    }

}