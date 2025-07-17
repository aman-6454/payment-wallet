package com.paymentwallet.payment_wallet.exception;

public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException(String msg){
        super(msg);
    }
}
