package com.paymentwallet.payment_wallet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import com.paymentwallet.payment_wallet.dao.CurrencyRepository;
import com.paymentwallet.payment_wallet.dao.TransactionRepository;
import com.paymentwallet.payment_wallet.dao.UserRepository;
import com.paymentwallet.payment_wallet.dao.WalletRepository;
import com.paymentwallet.payment_wallet.exception.EmailException;
import com.paymentwallet.payment_wallet.exception.InsufficientBalanceException;
import com.paymentwallet.payment_wallet.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void testCreateAccount_Success() {
        UserDTO userDTO = new UserDTO("pass123", "john@example.com", "john");

        when(userRepository.findByEmailId("john@example.com")).thenReturn(Optional.empty());

        Currency currency = new Currency();
        currency.setCurrencyId(1);
        currency.setAbbreviation("INR");

        when(currencyRepository.findByCurrencyId(1)).thenReturn(Optional.of(currency));

        User savedUser = new User();
        savedUser.setUsername("john");
        savedUser.setEmailId("john@example.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = walletService.createAccount(userDTO);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepository).save(any(User.class));
    }


    @Test
    void testCreateAccount_EmailAlreadyExists() {
        UserDTO userDTO = new UserDTO("john", "john@example.com", "123456789");
        when(userRepository.findByEmailId("john@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailException.class, () -> walletService.createAccount(userDTO));
    }


    @Test
    void testCreateAccount_CurrencyNotFound() {
        UserDTO userDTO = new UserDTO("john", "john@example.com", "12345678");

        when(userRepository.findByEmailId("john@example.com")).thenReturn(Optional.empty());
        when(currencyRepository.findByCurrencyId(1)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = walletService.createAccount(userDTO);

        assertNull(createdUser.getWallet().getCurrency());
    }



    @Test
    void testAddAmount_Success() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100.0);
        wallet.setUser(new User());

        when(walletRepository.findByUser_UserId(1)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet updatedWallet = walletService.addAmount(1, 50.0);

        assertEquals(150.0, updatedWallet.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testGetBalance_Success() {
        Wallet wallet = new Wallet();
        wallet.setBalance(200.0);
        wallet.setCurrency(new Currency("Rs"));
        User user = new User();
        user.setUserId(1);
        wallet.setUser(user);

        when(walletRepository.findByUser_UserId(1)).thenReturn(Optional.of(wallet));

        Map<String, Object> balanceMap = walletService.getBalance(1);

        assertEquals(1, balanceMap.get("userId"));
        assertEquals(200.0, balanceMap.get("balance"));
        assertEquals("Rs", balanceMap.get("currency"));
    }

    @Test
    void testTransferFunds_Success() {
        Wallet sender = new Wallet();
        sender.setBalance(500.0);

        Wallet receiver = new Wallet();
        receiver.setBalance(300.0);

        TransferDTO dto = new TransferDTO();
        dto.setSenderWalletId(1);
        dto.setReceiverWalletId(2);
        dto.setAmount(100.0);

        when(walletRepository.findById(1)).thenReturn(Optional.of(sender));
        when(walletRepository.findById(2)).thenReturn(Optional.of(receiver));

        walletService.transferFunds(dto);

        assertEquals(400.0, sender.getBalance());
        assertEquals(400.0, receiver.getBalance());
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(walletRepository).save(sender);
        verify(walletRepository).save(receiver);
    }

    @Test
    void testTransferFunds_ThrowsInsufficientBalanceException() {
        // Arrange
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setSenderWalletId(1);
        transferDTO.setReceiverWalletId(2);
        transferDTO.setAmount(100.0);

        Wallet senderWallet = new Wallet();
        senderWallet.setWalletId(1);
        senderWallet.setBalance(50.0); // Less than transfer amount

        Wallet receiverWallet = new Wallet();
        receiverWallet.setWalletId(2);
        receiverWallet.setBalance(200.0);

        when(walletRepository.findById(1)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2)).thenReturn(Optional.of(receiverWallet));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            walletService.transferFunds(transferDTO);
        });
    }

}
