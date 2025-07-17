package com.paymentwallet.payment_wallet.dao;

import com.paymentwallet.payment_wallet.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Optional<Currency> findByCurrencyId(Integer currencyid);

}
