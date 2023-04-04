package az.unibank.unitechapp.service.impl;

import az.unibank.unitechapp.exception.CustomErrorException;
import az.unibank.unitechapp.model.Currency;
import az.unibank.unitechapp.repository.CurrencyRepository;
import az.unibank.unitechapp.request.CurrencyRequest;
import az.unibank.unitechapp.response.CurrencyResponse;
import az.unibank.unitechapp.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrencyServiceImplTest {

    private String token="eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2Nj" +
            "EwODQ5MTEsImV4cCI6MTY2MTA4ODUxMSwic3ViIjoiMSJ9.ami9TOUpVJi5X" +
            "dvXrwFE5in_f3EF8Q9cxh-tVK1-6DiWfDHsCfDTDAZ4mY-TrQ49ilDv5e1uCcoxG-BzLH1LtA";

    //@Mock
    private CurrencyRepository currencyRepository;
    //@Mock
    private JwtTokenUtil jwtTokenUtil;
    // @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    public void setUp() {
        currencyRepository = mock(CurrencyRepository.class);
        jwtTokenUtil = mock(JwtTokenUtil.class);
        currencyService = new CurrencyServiceImpl(currencyRepository,jwtTokenUtil);
        when(jwtTokenUtil.validateToken(token)).thenReturn(true);
    }

    @Test
    public void testCurrencyRate(){
       Currency currency=Currency.builder()
                .id(1)
                .code("AZN")
                .rate(1.70)
                .createdDate(LocalDateTime.now())
                .build();
        when(currencyRepository.findAll()).thenReturn(Arrays.asList(currency));

        List<CurrencyRequest> currencyDtoList= currencyService.currencyRate(token);

        assertNotNull(currencyDtoList);
        assertEquals(1,currencyDtoList.size());

    }

    @Test
    public void testExchangeCurrency(){
        String from="AZN";
        String to="TL";
        Currency currency=Currency.builder()
                .code("AZN")
                .rate(1.70)
                .build();

        when(currencyRepository.findByCode(from)).thenReturn(Optional.of(currency));
        when(currencyRepository.findByCode(to)).thenReturn(Optional.of(currency));

        CurrencyResponse currencyResponse=currencyService.exchangeCurrency(from,to,token);

        assertNotNull(currencyResponse);
        assertEquals("AZN",currencyResponse.getFrom());

    }

    @Test
    public void testExchangeCurrencyFailure(){
        Exception ex= assertThrows(CustomErrorException.class,()-> {
            String from = "ZZZ";
            String to = "TL";
            Currency currency = Currency.builder()
                    .code("AZN")
                    .rate(1.70)
                    .build();

            when(currencyRepository.findByCode(from)).thenReturn(Optional.of(currency));
            when(currencyRepository.findByCode(to)).thenReturn(Optional.of(currency));

            CurrencyResponse currencyResponse = currencyService.exchangeCurrency(from, to, token);

            assertNotNull(currencyResponse);
            assertEquals("AZN", currencyResponse.getFrom());
        });
        assertEquals("currency code is not right",ex.getMessage());

    }

}