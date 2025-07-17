package com.paymentwallet.payment_wallet.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {


    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email id not in required format")
    @NotBlank(message = "Email is required")
    private String emailId;


    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;


    public UserDTO() {
        super();
    }

    public UserDTO(String username,	String emailId,String password) {
        super();
        this.username = username;
        this.emailId = emailId;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
