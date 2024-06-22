package com.testNisum.service;

import com.testNisum.exception.EmailAlreadyExistsException;
import com.testNisum.model.User;
import com.testNisum.model.UserResponseDto;
import com.testNisum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /*public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        return userRepository.save(user);
    }*/
    public UserResponseDto createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Generar un token, esto puede ser un JWT u otro tipo de token
        String token = UUID.randomUUID().toString();
        user.setToken(token);

        User createdUser = userRepository.save(user);

        return new UserResponseDto(
                createdUser.getId(),
                createdUser.getCreatedAt(),
                createdUser.getUpdatedAt(),
                createdUser.getLastLogin(),
                createdUser.getToken(),
                createdUser.getIsActive()
        );
    }

    public UserResponseDto updateUser(UUID id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());

        User updatedUser = userRepository.save(user);

        return new UserResponseDto(
                updatedUser.getId(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt(),
                updatedUser.getLastLogin(),
                updatedUser.getToken(),
                updatedUser.getIsActive()
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
