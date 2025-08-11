package com.campana.email.email_campaigner_api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campana.email.email_campaigner_api.dto.UserDto;
import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.UserRepository;
import com.campana.email.email_campaigner_api.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	@Autowired
    private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
    // Endpoint para obtener la información del usuario autenticado
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(userDto);
    }

    // Endpoint para obtener todos los usuarios (solo ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserDto> userDtos = users.stream()
            .map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    // Endpoint para obtener un usuario por ID (solo ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
            .map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar un usuario (solo ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        return userService.findUserById(id)
            .map(existingUser -> {
                existingUser.setUsername(userDetails.getUsername());
                existingUser.setEmail(userDetails.getEmail());
                // ✅ Actualizar la contraseña solo si se proporciona una nueva
                if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                    existingUser.setPassword(userDetails.getPassword());
                }
                User updatedUser = userService.saveUser(existingUser);
                return new UserDto(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail());
            })
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para eliminar un usuario (solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
