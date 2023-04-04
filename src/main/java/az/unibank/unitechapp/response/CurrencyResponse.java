package az.unibank.unitechapp.response;


import az.unibank.unitechapp.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {

    String from;
    String to;
    double rate;

    public CurrencyResponse(Currency currency) {
        this.rate=currency.getRate();
    }
}
