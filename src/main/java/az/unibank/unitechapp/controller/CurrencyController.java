package az.unibank.unitechapp.controller;

import az.unibank.unitechapp.request.CurrencyRequest;
import az.unibank.unitechapp.response.CurrencyResponse;
import az.unibank.unitechapp.service.CurrencyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
@ApiOperation("Currency Service")
public class CurrencyController {

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @GetMapping
    @ApiOperation("This return currency list")
    public ResponseEntity<List<CurrencyRequest>> currencyRate(@RequestHeader("Authorization") String bearer)
    {
        return ResponseEntity.ok(currencyService.currencyRate(bearer));
    }

    @GetMapping("/{from}/{to}")
    public ResponseEntity<CurrencyResponse> exchangeCurrency(@PathVariable String from, @PathVariable String to,
                                                             @RequestHeader("Authorization") String bearer) {
        return ResponseEntity.ok(currencyService.exchangeCurrency(from,to,bearer));
    }


}
