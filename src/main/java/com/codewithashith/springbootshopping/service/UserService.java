package com.codewithashith.springbootshopping.service;

import com.codewithashith.springbootshopping.dto.AuthDto;
import com.codewithashith.springbootshopping.exception.common.InvalidUserException;
import com.codewithashith.springbootshopping.model.AppUser;
import com.codewithashith.springbootshopping.model.Role;
import com.codewithashith.springbootshopping.repository.RoleRepository;
import com.codewithashith.springbootshopping.repository.UserRepository;
import com.codewithashith.springbootshopping.request.LoginRequest;
import com.codewithashith.springbootshopping.request.RegisterRequest;
import com.codewithashith.springbootshopping.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AuthDto authDto;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthResponse register(RegisterRequest registerRequest) {
        AppUser appUser = authDto.mapToAppUser(registerRequest);
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        appUser.setRoles(roleRepository.findByName(Role.USER));
        appUser = userRepository.save(appUser);
        return authDto.mapToAuthResponse(appUser);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        AppUser appUser = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidUserException("Invalid user credentials"));

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), appUser.getPassword())) {
            throw new InvalidUserException("Invalid password");
        }

        return authDto.mapToAuthResponse(appUser);
    }

}