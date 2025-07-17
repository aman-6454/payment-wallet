package com.paymentwallet.payment_wallet.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class InsufficientBalanceExceptionTest {


    @Test
    void testExceptionMessage() {
        String message = "Insufficient balance!";
        InsufficientBalanceException exception = new InsufficientBalanceException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionWithNullMessage() {
        InsufficientBalanceException exception = new InsufficientBalanceException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

}
