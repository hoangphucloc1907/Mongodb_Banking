package com.loczxph.banking.service;

import com.loczxph.banking.dto.ChangePasswordRequest;
import com.loczxph.banking.dto.UserResponse;
import com.loczxph.banking.dto.UserUpdateRequest;
import com.loczxph.banking.model.Role;
import com.loczxph.banking.model.User;

import java.security.Principal;
import java.util.List;
import java.util.Set;


public interface UserService {
    List<UserResponse> getAllUsers();

    void updateUser(UserUpdateRequest request, Principal connectedUser);

    public void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
