package com.paymentwallet.payment_wallet.dao;

import com.paymentwallet.payment_wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    Optional<Wallet> findByUser_UserId(Integer userId);
}