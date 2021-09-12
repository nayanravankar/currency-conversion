package com.currency.conversion.currencyconversion.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CurrencyConversion implements Serializable {
    private Long id;

    private String from;

    private String to;

    private BigDecimal conversionMultiple;

    private Date date;

    private BigDecimal totalAmount;
}
