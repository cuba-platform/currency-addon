package com.haulmont.addon.currency.service;

import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;

@Component(CurrencyRatesService.NAME)
public class CurrencyRatesServiceBean implements CurrencyRatesService {

    @Inject
    private DataManager dataManager;


    public boolean autoUpdateIsActive() {
        ScheduledTask task = loadTask();

        Boolean rawIsActive = task.getActive();
        return rawIsActive == null ? false : rawIsActive;
    }


    @Transactional
    public void setAutoUpdateActive(boolean isActive) {
        loadTask().setActive(isActive);
    }


    protected ScheduledTask loadTask() {
        return dataManager.load(ScheduledTask.class)
                .id(UUID.fromString("65dfa486-8c33-1844-268e-58aa85a4c10e"))
                .one();
    }
}
