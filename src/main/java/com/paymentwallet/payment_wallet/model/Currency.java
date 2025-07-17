package com.paymentwallet.payment_wallet.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
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

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
