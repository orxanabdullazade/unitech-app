package az.unibank.unitechapp.request;

import az.unibank.unitechapp.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRequest {

     String code;
     double rate;

     public CurrencyRequest(Currency currency) {
          this.code=currency.getCode();
          this.rate=currency.getRate();
     }
}


