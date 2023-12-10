package com.loczxph.banking.service.Impl;

import com.loczxph.banking.dto.ChangePasswordRequest;
import com.loczxph.banking.dto.UserResponse;
import com.loczxph.banking.dto.UserUpdateRequest;

import com.loczxph.banking.model.User;
import com.loczxph.banking.repository.AccountRepository;
import com.loczxph.banking.repository.UserRepository;
import com.loczxph.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }


    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserUpdateRequest request, Principal connectedUser) {
        User user = getUserFromPrincipal(connectedUser);

        // Cập nhật email nếu được cung cấp
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        // Cập nhật username nếu được cung cấp
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        // Lưu thông tin người dùng đã cập nhật
        userRepository.save(user);
    }

    private User getUserFromPrincipal(Principal connectedUser) {
        if (connectedUser instanceof UsernamePasswordAuthenticationToken) {
            return (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        }
        throw new IllegalStateException("Invalid principal type");
    }


}
