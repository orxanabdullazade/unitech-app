package az.unibank.unitechapp.service.impl;

import az.unibank.unitechapp.exception.CustomErrorException;
import az.unibank.unitechapp.exception.ErrorCodeEnum;
import az.unibank.unitechapp.model.Currency;
import az.unibank.unitechapp.repository.CurrencyRepository;
import az.unibank.unitechapp.request.CurrencyRequest;
import az.unibank.unitechapp.response.CurrencyResponse;
import az.unibank.unitechapp.service.CurrencyService;
import az.unibank.unitechapp.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public List<CurrencyRequest> currencyRate(String bearer) {
        if ( !jwtTokenUtil.validateToken(bearer)) throw new CustomErrorException(ErrorCodeEnum.UNAUTHORIZED);

        List<Currency> currencyList=currencyRepository.findAll();

        return currencyList.stream().map(currency -> new CurrencyRequest(currency)).collect(Collectors.toList());

    }

    @Override
    public CurrencyResponse exchangeCurrency(String from, String to,String bearer) {
        if ( !jwtTokenUtil.validateToken(bearer)) throw new CustomErrorException(ErrorCodeEnum.UNAUTHORIZED);

        List<String> codes = new ArrayList<>();
        codes.add("AZN");
        codes.add("TL");
        codes.add("USD");
        codes.add("GBR");
        codes.add("AED");

        if (codes.contains(from) && codes.contains(to)) {

            Currency currencyFrom = currencyRepository.findByCode(from).get();
            Currency currencyTo = currencyRepository.findByCode(to).get();

            double rate = 1.0;

            if (from == "USD" && to == "USD") {
                rate = 1;
            }
            if (from == "USD" && to != "USD") {
                rate = currencyTo.getRate();
            }
            if (from != "USD" && to == "USD") {
                rate = 1 / currencyFrom.getRate();
            }
            if (from != "USD" && to != "USD") {
                rate = currencyTo.getRate() / currencyFrom.getRate();
            }

            return CurrencyResponse.builder()
                    .from(currencyFrom.getCode())
                    .to(currencyTo.getCode())
                    .rate(rate)
                    .build();

        } else {
            throw new CustomErrorException(ErrorCodeEnum.CURRENCY_CODE_NOT_FOUND);
        }
    }
}
