package com.paymentwallet.payment_wallet.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandleTest {


    @Test
    void testHandleValidationErrors() throws NoSuchMethodException {
        // Arrange
        GlobalExceptionHandle handler = new GlobalExceptionHandle();

        // Mock BindingResult and FieldError
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("user", "username", "Username is required");
        FieldError fieldError2 = new FieldError("user", "email", "Email is invalid");

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        // Mock MethodArgumentNotValidException
        Method method = this.getClass().getDeclaredMethod("testHandleValidationErrors");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }

    @Test
    void testHandleTypeMismatch() {
        // Arrange
        GlobalExceptionHandle handler = new GlobalExceptionHandle();
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", Integer.class, "userId", null, new IllegalArgumentException("Invalid type")
        );

        // Act
        ResponseEntity<String> response = handler.handleTypeMismatch(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request. User id cannot contain characters.", response.getBody());
    }


    @Test
     void testHandleRuntimeException() {
        // Arrange
        GlobalExceptionHandle handler = new GlobalExceptionHandle();
        RuntimeException ex = new RuntimeException("Something went wrong");

        // Act
        ResponseEntity<String> response = handler.handleRuntimeException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody());
    }

}
