package com.loczxph.banking.dto;

import com.loczxph.banking.model.Account;
import com.loczxph.banking.model.Role;
import com.loczxph.banking.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private String email;
    private Role role;
    private String accountNumber;


    public static UserResponse fromUser(User user) {
        Account account = user.getAccount();
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                account != null ? account.getAccountNumber() : null
//                account != null ? account.getBalance() : null
        );
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
