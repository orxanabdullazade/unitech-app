package az.unibank.unitechapp.repository;

import az.unibank.unitechapp.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

       List<Currency> findCurrencyByCreatedDateLessThan(LocalDateTime localDateTime);

       Optional<Currency> findByCode(String code);

}
