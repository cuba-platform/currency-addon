# currency-addon
Currency CUBA addon provides currencies and currency rates for CUBA application. Component is under development.

# Content
* [Global Application Part]()
    * [Entities](#entities)
    * [Services](#services)
* [Core Part](#core-part)
* [Web Part](#web-part)
    * [Predefined Screens](#predefined-screens)
    * [Field Usage](#field-usage)
        * [Auto Replace](#auto-replace)
        * [XML Descriptor](#xml-descriptor)
        * [Programmatic](#programmatic)
* [Currency Rates](#currency-rates)
    * [Scheduled Task](#scheduled-task)
    * [Providers](#providers)
    * [Fixer.io](#fixer-io)
* [Configuration](#configuration)
* [Usage Quickstart](#usage-quickstart)
    * [1. Create currencies](#1-create-currencies)
    * [2. Configure rate source](#2-configure-rate-source)
    * [3. Create own entity](#3-create-own-entity)
* [Demo Project](#demo-project)

    
    
# Global Application Part
[`CurrencyRateAware`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyRateAware.java) - is __interface__, amount of specific currency at specific date. 



## Entities
* [`CurrencyDescriptor`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyDescriptor.java) - persist currency from real world like USD, EUR, BTC, ...
* [`Currency`](modules/global/src/com/haulmont/addon/currency/entity/Currency.java) - implements [`CurrencyRateAware`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyRateAware.java).
* [`CurrencyRate`](modules/global/src/com/haulmont/addon/currency/entity/Currency.java) - rate for exchange from Currency1 to Currency2 at specific date.



## Services
* [`CurrencyBigDecimalFormat`](modules/global/src/com/haulmont/addon/currency/format/CurrencyBigDecimalFormat.java) - can format and parse amount value depends on user `locale` and currency `precision`.
* [`CurrencyService`](modules/global/src/com/haulmont/addon/currency/service/CurrencyService.java) - util service on global layer.



# Core Part
[`Currencies`](modules/core/src/com/haulmont/addon/currency/core/Currencies.java) - service helps to work with addon entities.



# Web Part



## Predefined Screens
* __Currencies__ - provide ability to manage currencies in system.
* __Rates__ - show existed exchange rates between currencies.



## Field Usage



### Auto Replace
UI field for entity field which implements interface [`CurrencyRateAware`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyRateAware.java) will be automatically replaced by [`CurrencyAddonField`](modules/web/src/com/haulmont/addon/currency/web/gui/components/currency_field/CurrencyAddonField.java).

F.e. we have entity `MyEntity` with field __sellAmount__ with type [`Currency`](modules/global/src/com/haulmont/addon/currency/entity/Currency.java) so in UI simple `com.haulmont.cuba.gui.components.TextField` will be replaced by [`CurrencyAddonField`](modules/web/src/com/haulmont/addon/currency/web/gui/components/currency_field/CurrencyAddonField.java).   
```xml
<fieldGroup datasource="myEntityDs">
    <column width="250px">
        <field property="name"/>
        <field property="sellAmount"/>
    </column>
</fieldGroup>
```



### XML Descriptor
Also we can define [`CurrencyAddonField`](modules/web/src/com/haulmont/addon/currency/web/gui/components/currency_field/CurrencyAddonField.java) in screen XML descriptor:
```xml
<currencyAddonField/>
```



### Programmatic
```java
import com.haulmont.addon.currency.web.gui.components.currency_field.CurrencyAddonField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
...
ComponentsFactory componentsFactory = ...;
CurrencyAddonField currencyField = componentsFactory.createComponent(CurrencyAddonField.class);
```



# Currency Rates
[`CurrencyRateWorkerMBean`](modules/core/src/com/haulmont/addon/currency/core/CurrencyRateWorkerMBean.java) - provides ability to update exchange rates immediately and some other actions.



## Scheduled Task
Addon has preconfigured scheduled task for update exchange rates.
It __Turned Of__ by default and has period __4 hours__. 



## Providers
[`CurrencyRateProvider`](modules/core/src/com/haulmont/addon/currency/core/CurrencyRateProvider.java) - is interface, it should provide exchange rates for specific currencies and date.



### Fixer.io
Currency addon has own implementation of [`CurrencyRateProvider`](modules/core/src/com/haulmont/addon/currency/core/CurrencyRateProvider.java) - [`FixerIOCurrencyRateProviderBean`](modules/core/src/com/haulmont/addon/currency/core/impl/FixerIOCurrencyRateProviderBean.java).

It uses REST API of [fixer.io](http://fixer.io) exchange rates provider.
This provider requires registration and providing API KEY to addon configuration.
It uses configuration interface [`FixerIOConfig`](modules/core/src/com/haulmont/addon/currency/core/impl/FixerIOConfig.java).

Required parameter: `addon.currency.fixerIO.apiKey`. 



# Configuration
Addon has some configuration interfaces:
* [`CurrencyApplicationProperties`](modules/core/src/com/haulmont/addon/currency/core/config/CurrencyApplicationProperties.java)
* [`CurrencyConfig`](modules/global/src/com/haulmont/addon/currency/config/CurrencyConfig.java)



# Usage Quickstart
Simple steps for work with currency addon:
1. Create currencies (by default exist only USD currency)
2. Configure rate source



## 1. Create currencies
By default exists only USD currency. Need to create other currencies required in your project.



## 2. Configure rate source
By default application doesn't have active providers. Need to implements self own provider or configure and activate existed.



For configure existed provider need to:
1. Register at [fixer.io](http://fixer.io)
2. Fetch `access key` at [fixer.io](http://fixer.io) profile.
3. Set `access key` in addon configuration.
4. Turn on scheduled task for auto update rated or call [`CurrencyRateWorkerMBean`](modules/core/src/com/haulmont/addon/currency/core/CurrencyRateWorkerMBean.java) method.



## 3. Create own entity
Now we can create entity with [`CurrencyRateAware`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyRateAware.java) ([`Currency`](modules/global/src/com/haulmont/addon/currency/entity/Currency.java)) field type.



# Demo Project
Addon has [Demo Project](https://github.com/cuba-platform/currency-addon-demo) for showing use cases.