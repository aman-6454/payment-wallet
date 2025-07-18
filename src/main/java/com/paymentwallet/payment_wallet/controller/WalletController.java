package com.paymentwallet.payment_wallet.controller;

import java.security.SecureRandom;
import java.util.Map;

import com.paymentwallet.payment_wallet.model.TransferDTO;
import com.paymentwallet.payment_wallet.model.UserDTO;
import com.paymentwallet.payment_wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class WalletController {


    private final WalletService walletService;

    public WalletController(WalletService walletService){
        this.walletService=walletService;
    }

    SecureRandom random = new SecureRandom();


    @PostMapping("/")
    public ResponseEntity<String> createAccount(@Valid @RequestBody UserDTO userDTO) {

        walletService.createAccount(userDTO);
        return new ResponseEntity<>("Wallet account created successfully",HttpStatus.CREATED);
    }


    @PatchMapping("/wallet/{userId}")
    public ResponseEntity<String> addAmount(@PathVariable Integer userId, @RequestParam Double amount) {
        walletService.addAmount(userId, amount);
        return new ResponseEntity<>("Balance added successfully",HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Integer userId)  {
        Map<String, Object> balance = walletService.getBalance(userId);
        return new ResponseEntity<>(balance,HttpStatus.OK);
    }


    @PostMapping("/wallet/transfer")
    public ResponseEntity<String> transferFunds( @RequestBody TransferDTO transferDTO) {
        walletService.transferFunds(transferDTO);
        return new ResponseEntity<>("Transfer done successfully. Use transfer id "+ random.nextInt(100000, 1000000) +" for further reference.",HttpStatus.OK);
    }

}

