package com.gerop.stockcontrol.jewelry.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gerop.stockcontrol.jewelry.model.entity.User;
import com.gerop.stockcontrol.jewelry.repository.UserRepository;

@Service
public class UserServiceHelper {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
