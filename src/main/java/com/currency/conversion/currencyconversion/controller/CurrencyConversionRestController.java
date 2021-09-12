package com.currency.conversion.currencyconversion.controller;

import com.currency.conversion.currencyconversion.bean.CurrencyConversion;
import com.currency.conversion.currencyconversion.proxy.CurrencyConversionProxy;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionRestController {

    private Logger logger = LoggerFactory.getLogger(CurrencyConversionRestController.class);

    @Autowired
    private CurrencyConversionProxy currencyConversionProxy;

    @GetMapping("/{from}/to/{to}/quantity/{quantity}")
    @Retry(name="default",fallbackMethod = "genericFalloutMethod")
    public CurrencyConversion getCurrencyConversionValue(@PathVariable("from") String from,
                                           @PathVariable("to") String to,
                                           @PathVariable("quantity") String quantity){

        Map<String,String> uriParam = new HashMap<>();
        uriParam.put("from","USD");
        uriParam.put("to","INR");

        ResponseEntity<CurrencyConversion> currencyConversionResponseEntity = new RestTemplate().getForEntity("http://localhost:8081/currency-exchange/{from}/to/{to}",
                CurrencyConversion.class,
                uriParam);

        CurrencyConversion currencyConversion = currencyConversionResponseEntity.getBody();
        currencyConversion.setTotalAmount(currencyConversion.getConversionMultiple().multiply(BigDecimal.valueOf(Long.parseLong(quantity))));

        return currencyConversion;
    }

    @GetMapping("/feign/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion getCurrencyConversionValueFeign(@PathVariable("from") String from,
                                                         @PathVariable("to") String to,
                                                         @PathVariable("quantity") String quantity){

        CurrencyConversion currencyConversion = currencyConversionProxy.getExchnageRate(from,to);;
        currencyConversion.setTotalAmount(currencyConversion.getConversionMultiple().multiply(BigDecimal.valueOf(Long.parseLong(quantity))));
        return currencyConversion;
    }


    /*
    * @retry : resilience4j enable retry if case of any exception before throwing exception
    * name : define the of instance, we can define multiple instance based on business need
    * fallbackmethod : method to execute if fail after all retry attempts
    * */
    @GetMapping("/test/retry")
    @Retry(name ="default", fallbackMethod = "genericFalloutMethod")
    public  String getResiliance4jRetry(){
        logger.info("in test resiliance4j retry method");
        ResponseEntity<Object> obj= new RestTemplate().getForEntity("http://localhost:8080/unknown-url",null,String.class);
        return obj.getBody().toString();
    }

    public String genericFalloutMethod(Exception ex){
        return "We are under maintainance, sorry for inconviences";
    }
}
