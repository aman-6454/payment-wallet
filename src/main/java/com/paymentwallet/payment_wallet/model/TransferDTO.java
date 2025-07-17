package com.paymentwallet.payment_wallet.model;

public class TransferDTO {


    private Integer senderWalletId;

    private Integer receiverWalletId;

    private Double amount;


    public Integer getSenderWalletId() {
        return senderWalletId;
    }
    public void setSenderWalletId(Integer senderWalletId) {
        this.senderWalletId = senderWalletId;
    }
    public Integer getReceiverWalletId() {
        return receiverWalletId;
    }
    public void setReceiverWalletId(Integer receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }



}
