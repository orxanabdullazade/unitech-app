package az.unibank.unitechapp.service;

import az.unibank.unitechapp.request.CurrencyRequest;
import az.unibank.unitechapp.response.CurrencyResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CurrencyService {

    List<CurrencyRequest> currencyRate(String bearer);

    CurrencyResponse exchangeCurrency(String from, String to,String bearer);

}
