package com.loczxph.banking.controller;


import com.loczxph.banking.dto.ChangePasswordRequest;
import com.loczxph.banking.dto.UserUpdateRequest;
import com.loczxph.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        try {
            service.changePassword(request, connectedUser);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-info")
    public ResponseEntity<String> updateUserInfo(@RequestBody UserUpdateRequest  request, Principal principal) {
        try {
            service.updateUser(request, principal);
            return ResponseEntity.ok("User information updated successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
