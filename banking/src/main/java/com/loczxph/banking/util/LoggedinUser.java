package com.loczxph.banking.util;

import com.loczxph.banking.exception.NotFoundException;
import com.loczxph.banking.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class LoggedinUser {
    public static String getAccountNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                User user = (User) principal;
                return user.getAccount().getAccountNumber();
            }
        }
        throw new NotFoundException("Account number not found in Security Context.");
    }
}
