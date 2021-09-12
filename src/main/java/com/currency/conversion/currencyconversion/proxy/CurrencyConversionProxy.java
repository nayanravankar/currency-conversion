package com.currency.conversion.currencyconversion.proxy;

import com.currency.conversion.currencyconversion.bean.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
    Url not need in case of eureka naming-server -- @FeignClient(name = "currency-exchange",url = "localhost:8081","url")
    Feign cliend will do LB for with naming sever
 */
//@FeignClient(name = "currency-exchange",url = "localhost:8081")
@FeignClient(name = "currency-exchange")
public interface CurrencyConversionProxy {

    @GetMapping("/currency-exchange/{from}/to/{to}")
    public CurrencyConversion getExchnageRate(@PathVariable("from") String from, @PathVariable("to") String to );

}
