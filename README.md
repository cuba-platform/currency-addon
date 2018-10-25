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
* [...]()
        


    
    
# Global Application Part

[`CurrencyRateAware`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyRateAware.java) - is __interface__, amount of specific currency at specific date. 

## Entities
* [`CurrencyDescriptor`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyDescriptor.java) - persist currency from real world like USD, EUR, BTC, ...
* [`Currency`](modules/global/src/com/haulmont/addon/currency/entity/Currency.java) - implements [`CurrencyRateAware`](modules/global/src/com/haulmont/addon/currency/entity/CurrencyRateAware.java).
* [`CurrencyRate`](modules/global/src/com/haulmont/addon/currency/entity/Currency.java) - rate for exchange from Currency1 to Currency2 at specific date.



## Services
[`CurrencyBigDecimalFormat`](modules/global/src/com/haulmont/addon/currency/format/CurrencyBigDecimalFormat.java) - can format and parse amount value depends on user `locale` and currency `precision`.
[`CurrencyService`](modules/global/src/com/haulmont/addon/currency/service/CurrencyService.java) - util service on global layer.

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

# Currency Rates Source
[`CurrencyRateProvider`](modules/core/src/com/haulmont/addon/currency/core/CurrencyRateProvider.java) - provide exchange rates for specific currencies and date.
[`CurrencyRateWorkerMBean`](modules/core/src/com/haulmont/addon/currency/core/CurrencyRateWorkerMBean.java) - provide ability to do some system actions.

## Fixer.io

# Job

# Configuration
`CurrencyApplicationProperties`
`CurrencyConfig`

# Usage Quickstart
1. Create currency
2. Configure rate source

# Demo Project

# Development
`gradlew checkstyleMain test install`