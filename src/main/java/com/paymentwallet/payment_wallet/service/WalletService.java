package com.paymentwallet.payment_wallet.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.paymentwallet.payment_wallet.dao.CurrencyRepository;
import com.paymentwallet.payment_wallet.dao.TransactionRepository;
import com.paymentwallet.payment_wallet.dao.UserRepository;
import com.paymentwallet.payment_wallet.dao.WalletRepository;
import com.paymentwallet.payment_wallet.exception.EmailException;
import com.paymentwallet.payment_wallet.exception.InsufficientBalanceException;
import com.paymentwallet.payment_wallet.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class WalletService {

    public static final Integer CURRENCY_ID = 1;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private CurrencyRepository currencyRepository;


    public User createAccount(UserDTO userDTO){
        log.info("Create Account with user details: {}", userDTO);
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmailId(userDTO.getEmailId());

        if(userRepository.findByEmailId(user.getEmailId()).isPresent()){
            throw new EmailException("Email Already Registered!");
        }

        Wallet wallet = new Wallet();

        Optional<Currency> optionalCurrency = currencyRepository.findByCurrencyId(WalletService.CURRENCY_ID);
        if(optionalCurrency.isPresent()){
            Currency currency = optionalCurrency.get();
            wallet.setCurrency(currency);
        }

        wallet.setBalance(0.0);
        wallet.setUser(user);
        user.setWallet(wallet);

        log.info("User Created Successfully !");
        return userRepository.save(user);

    }

    public Wallet addAmount(Integer userId, Double amount) {

        Wallet wallet = walletRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        wallet.setBalance(wallet.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());
        transaction.setStatus("CREDIT");
        transaction.setTime(LocalTime.now());
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);

        return walletRepository.save(wallet);

    }


    public Map<String,Object> getBalance(Integer userId)  {


        Wallet wallet = walletRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String,Object> map = new HashMap<>();
        map.put("userId", wallet.getUser().getUserId());
        map.put("balance", wallet.getBalance());
        map.put("currency", wallet.getCurrency().getAbbreviation());


        return map;
    }


    public void transferFunds(TransferDTO transferDTO) {
        Wallet senderWallet = walletRepository.findById(transferDTO.getSenderWalletId())
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));
        Wallet receiverWallet = walletRepository.findById(transferDTO.getReceiverWalletId())
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        if (senderWallet.getBalance() < transferDTO.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance. Kindly review your balance.");
        }

        senderWallet.setBalance(senderWallet.getBalance() - transferDTO.getAmount());
        receiverWallet.setBalance(receiverWallet.getBalance() + transferDTO.getAmount());

        Transaction fromWalletTransaction = new Transaction(senderWallet,"DEBIT",transferDTO.getAmount(),LocalDate.now(),LocalTime.now());
        Transaction toWalletTransaction = new Transaction(receiverWallet,"CREDIT",transferDTO.getAmount(),LocalDate.now(),LocalTime.now());

        transactionRepository.save(fromWalletTransaction);
        transactionRepository.save(toWalletTransaction);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
    }


}
