package com.campana.email.email_campaigner_api.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;

	  @Override
	    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
	        System.out.println("Intentando autenticar con: " + usernameOrEmail); // <-- Línea de depuración

	        User user = userRepository.findByUsername(usernameOrEmail)
	            .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
	                .orElseThrow(() -> {
	                    System.out.println("❌ Usuario no encontrado: " + usernameOrEmail); // <-- Mensaje de error
	                    return new UsernameNotFoundException("Usuario no encontrado con nombre de usuario o email: " + usernameOrEmail);
	                }));

	        System.out.println("✅ Usuario encontrado: " + user.getUsername()); // <-- Mensaje de éxito

	        return new org.springframework.security.core.userdetails.User(
	            user.getUsername(),
	            user.getPassword(),
	            user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList())
	        );
	    }
}