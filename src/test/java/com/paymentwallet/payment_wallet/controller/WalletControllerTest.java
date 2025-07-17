package com.paymentwallet.payment_wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.paymentwallet.payment_wallet.model.TransferDTO;
import com.paymentwallet.payment_wallet.model.User;
import com.paymentwallet.payment_wallet.model.UserDTO;
import com.paymentwallet.payment_wallet.model.Wallet;
import com.paymentwallet.payment_wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Wallet wallet;

    Random random = new Random();

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("Alice");
        user.setEmailId("alice@example.com");

        wallet = new Wallet();
        wallet.setWalletId(1);
        wallet.setBalance(100.0);
        wallet.setUser(user);
        user.setWallet(wallet);
    }

    @Test
    void testCreateAccount_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Alice");
        userDTO.setEmailId("alice@example.com");
        userDTO.setPassword("12345678");

        Mockito.when(walletService.createAccount(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddAmount_Success() throws Exception {
        Mockito.when(walletService.addAmount(1, 50.0)).thenReturn(wallet);

        mockMvc.perform(patch("/api/v1/users/wallet/1")
                        .param("amount", "50.0"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBalance_Success() throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("userId", wallet.getUser().getUserId());
        map.put("balance", wallet.getBalance());
        Mockito.when(walletService.getBalance(1)).thenReturn(map);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testTransferFunds_Success() throws Exception {

        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setSenderWalletId(1);
        transferDTO.setReceiverWalletId(2);
        transferDTO.setAmount(123456.0);

        mockMvc.perform(post("/api/v1/users/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDTO)))
                .andExpect(status().isOk());
    }
}

