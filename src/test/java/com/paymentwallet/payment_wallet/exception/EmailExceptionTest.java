package com.paymentwallet.payment_wallet.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class EmailExceptionTest {


    @Test
    void testEmailExceptionMessage() {
        String errorMessage = "Email Already Registered!";
        EmailException exception = new EmailException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testEmailExceptionWithoutMessage() {
        EmailException exception = new EmailException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

}
