package com.haulmont.addon.currency.service;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.ScheduledTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;

@Service(CurrencyRatesService.NAME)
public class CurrencyRatesServiceBean implements CurrencyRatesService {

    @Inject
    private Persistence persistence;


    @Override
    @Transactional
    public boolean autoUpdateIsActive() {
        ScheduledTask task = loadTask();

        Boolean rawIsActive = task.getActive();
        return rawIsActive == null ? false : rawIsActive;
    }


    @Override
    @Transactional
    public void setAutoUpdateActive(boolean isActive) {
        loadTask().setActive(isActive);
    }


    protected ScheduledTask loadTask() {
        EntityManager entityManager = persistence.getEntityManager();
        return entityManager.find(ScheduledTask.class, UUID.fromString("65dfa486-8c33-1844-268e-58aa85a4c10e"));
    }
}
