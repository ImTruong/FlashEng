package com.education.flashEng.service.Impl;

import com.education.flashEng.entity.UserEntity;
import com.education.flashEng.exception.DatabaseOperationException;
import com.education.flashEng.exception.ResourceAlreadyExistsException;
import com.education.flashEng.payload.request.RegisterRequest;
import com.education.flashEng.payload.response.ApiResponse;
import com.education.flashEng.repository.UserRepository;
import com.education.flashEng.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public ApiResponse<String> register(RegisterRequest registerRequest) {

        if (userRepository.existsByUsernameAndStatus(registerRequest.getUsername(), 1))
            throw new ResourceAlreadyExistsException("Username is already taken");

        if (userRepository.existsByEmailAndStatus(registerRequest.getEmail(), 1))
            throw new ResourceAlreadyExistsException("Email is already taken");

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        UserEntity user = modelMapper.map(registerRequest, UserEntity.class);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseOperationException("Failed to save user: Database constraint violated");
        } catch (Exception ex) {
            throw new DatabaseOperationException("An unexpected error occurred while saving user");
        }

        return new ApiResponse<>(true, "Registration successful", null);
    }
}
