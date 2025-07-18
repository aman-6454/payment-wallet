package com.paymentwallet.payment_wallet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Currency {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int currencyId;

    private String name;
    private String abbreviation;

    @OneToMany(mappedBy = "currency")
    private List<Wallet> wallets;

    public Currency() {
        super();
    }

    public Currency(String abbreviation) {
        this.abbreviation=abbreviation;
    }
}
