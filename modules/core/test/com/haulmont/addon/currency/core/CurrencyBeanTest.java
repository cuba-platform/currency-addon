package com.haulmont.addon.currency.core;

import com.haulmont.addon.currency.core.integration_tests.CurrencyIntegrationTestUtil;
import com.haulmont.addon.currency.entity.*;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CurrencyBeanTest {

    @ClassRule
    public static TestContainer testContainer = CurrencyIntegrationTestUtil.INSTANCE.initializeHsqlDatabaseAndApplication();
    private static Date rateActualDate;

    @Inject
    private CurrencyAPI api = AppContext.getApplicationContext().getBean(CurrencyAPI.class);

    static Metadata metadata;

    static Set<CurrencyDescriptor> activeCurrencies = new HashSet<>();
    static Set<CurrencyDescriptor> allCurrencies = new HashSet<>();

    static CurrencyDescriptor currency1;
    static CurrencyDescriptor currency2;
    static CurrencyDescriptor currency3;

    static CurrencyRate rateCurrency1toCurrency2;

    @BeforeClass
    public static void setUp() throws Exception {
        Transaction transaction = testContainer.persistence().createTransaction();
        metadata = testContainer.metadata();

        rateActualDate = new SimpleDateFormat("HH:mm dd-MM-yyyy").parse("10:20 21-08-2018");

        transaction.execute((Transaction.Callable<Void>) em -> {
            fillCurrencies(em);
            fillCurrencyRates(em);
            return null;
        });
    }


    private static void fillCurrencyRates(EntityManager em) {
        rateCurrency1toCurrency2 = metadata.create(CurrencyRate.class);
        rateCurrency1toCurrency2.setCurrency(currency1);
        rateCurrency1toCurrency2.setTargetCurrency(currency2);
        rateCurrency1toCurrency2.setDate(rateActualDate);
        rateCurrency1toCurrency2.setRate(BigDecimal.valueOf(2));
        em.persist(rateCurrency1toCurrency2);

        CurrencyRate rateCurrency1toCurrency3 = metadata.create(CurrencyRate.class);
        rateCurrency1toCurrency3.setCurrency(currency1);
        rateCurrency1toCurrency3.setTargetCurrency(currency3);
        rateCurrency1toCurrency3.setDate(rateActualDate);
        rateCurrency1toCurrency3.setRate(BigDecimal.valueOf(3));
        em.persist(rateCurrency1toCurrency3);
    }


    private static void fillCurrencies(EntityManager em) {
        currency1 = metadata.create(CurrencyDescriptor.class);
        currency1.setName("Name1");
        currency1.setCode("C1");
        currency1.setActive(true);
        em.persist(currency1);
        activeCurrencies.add(currency1);
        allCurrencies.add(currency1);

        currency2 = metadata.create(CurrencyDescriptor.class);
        currency2.setName("Name2");
        currency2.setCode("C2");
        currency2.setActive(true);
        em.persist(currency2);
        activeCurrencies.add(currency2);
        allCurrencies.add(currency2);

        currency3 = metadata.create(CurrencyDescriptor.class);
        currency3.setName("Name3");
        currency3.setCode("C3");
        currency3.setActive(false);
        em.persist(currency3);
        allCurrencies.add(currency3);
    }


    @Test
    public void testGetActiveCurrencies() {
        List<CurrencyDescriptor> resultActiveCurrencies = api.getActiveCurrencies();
        Assert.assertEquals(activeCurrencies, new HashSet<>(resultActiveCurrencies));
    }


    @Test
    public void testGetAllCurrencies() {
        List<CurrencyDescriptor> resultAllCurrencies = api.getAllCurrencies();
        Assert.assertEquals(allCurrencies, new HashSet<>(resultAllCurrencies));
    }


    @Test
    public void testConvertAmountWithValue() {
        BigDecimal value = BigDecimal.valueOf(4);
        BigDecimal expectedResult = value.multiply(rateCurrency1toCurrency2.getRate());

        CurrencyRateAware currencyValue = metadata.create(Currency.class);
        currencyValue.setDate(rateActualDate);
        currencyValue.setCurrency(currency1);
        currencyValue.setValue(value);

        BigDecimal resultAmount = api.convertAmount(currencyValue, currency2);

        int compareResult = expectedResult.compareTo(resultAmount);
        Assert.assertEquals(0, compareResult);
    }


    @Test
    public void testConvertAmountToCurrentRate() {
        BigDecimal value = BigDecimal.valueOf(3);
        BigDecimal actualResult = api.convertAmountToCurrentRate(value, currency1, currency2);

        BigDecimal expectedResult = value.multiply(rateCurrency1toCurrency2.getRate());

        int compareResult = expectedResult.compareTo(actualResult);
        Assert.assertEquals(0, compareResult);
    }


    @Test
    public void testConvertAmount() {
        BigDecimal value = BigDecimal.valueOf(3);

        BigDecimal actualValue = api.convertAmount(value, rateActualDate, currency1, currency2);

        BigDecimal expectedValue = value.multiply(rateCurrency1toCurrency2.getRate());

        int compareResult = expectedValue.compareTo(actualValue);
        Assert.assertEquals(0 , compareResult);
    }


    @Test
    public void testGetCurrencyByCode() {
        CurrencyDescriptor actualResult = api.getCurrencyByCode(currency3.getCode());

        Assert.assertEquals(currency3, actualResult);
    }


    @Test
    public void testGetLocalRate() {
        CurrencyRate actualResult = api.getLocalRate(rateActualDate, currency1, currency2);

        Assert.assertEquals(rateCurrency1toCurrency2, actualResult);
    }


    @Test
    public void testGetLocalRateForAbsentRate() {
        CurrencyRate result = api.getLocalRate(rateActualDate, currency2, currency3);

        Assert.assertNull(result);
    }


    @Test
    public void testConvertAmountToRateReverse() {
        BigDecimal value = BigDecimal.valueOf(6);

        BigDecimal actualResult = api.convertAmountToRateReverse(value, rateActualDate, currency2, currency1);

        BigDecimal expectedResult = value.divide(rateCurrency1toCurrency2.getRate(), RoundingMode.HALF_UP);
        int compareResult = expectedResult.compareTo(actualResult);
        Assert.assertEquals(0, compareResult);
    }
}