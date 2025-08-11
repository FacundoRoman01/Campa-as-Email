package com.campana.email.email_campaigner_api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campana.email.email_campaigner_api.dto.AuthResponseDto;
import com.campana.email.email_campaigner_api.dto.LoginDto;
import com.campana.email.email_campaigner_api.dto.RegisterDto;
import com.campana.email.email_campaigner_api.model.Role;
import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.RoleRepository;
import com.campana.email.email_campaigner_api.repository.UserRepository;
import com.campana.email.email_campaigner_api.security.JwtUtils;


@Service
public class AuthService {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;

	// Método para registrar un usuario
	public User registerUser(RegisterDto registerDto) {
		
		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new RuntimeException("Email already in use");
		}

		User user = new User();
		user.setEmail(registerDto.getEmail());
		user.setUsername(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		String roleName = registerDto.getRole() != null ? registerDto.getRole() : "ROLE_USER";
		Role role = roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		return userRepository.save(user);
	}
	
	// Método para el login y generación del token
	public AuthResponseDto login(LoginDto loginDto) {
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(
	            loginDto.getUsernameOrEmail(),
	            loginDto.getPassword()
	        )
	    );
	    
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
	    String token = jwtUtils.generateJwtToken(userDetails); 

	    // Paso 1: Obtener el objeto User completo desde el repositorio
	    // Se usa el nombre de usuario del userDetails para buscar al usuario en la base de datos
	    User userFromDetails = userRepository.findByUsername(userDetails.getUsername())
	        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

	    // Paso 2: Mapear los roles a una lista de Strings
	    List<String> roles = userFromDetails.getRoles().stream()
	        .map(Role::getName)
	        .collect(Collectors.toList());
	    
	    // Paso 3: Crear el DTO con todos los datos necesarios
	    return new AuthResponseDto(token, userFromDetails.getId(), userFromDetails.getEmail(), roles);
	}
}
