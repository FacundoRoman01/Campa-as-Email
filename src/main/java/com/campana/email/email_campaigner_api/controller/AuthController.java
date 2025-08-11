package com.campana.email.email_campaigner_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campana.email.email_campaigner_api.dto.AuthResponseDto;
import com.campana.email.email_campaigner_api.dto.LoginDto;
import com.campana.email.email_campaigner_api.dto.RegisterDto;
import com.campana.email.email_campaigner_api.dto.UserDto;
import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.UserRepository;
import com.campana.email.email_campaigner_api.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	  private final AuthService authService;
	  
	 

	    @Autowired
	    public AuthController(AuthService authService) {
	        this.authService = authService;
	        
	    }
	    
	    @PostMapping("/register")
	    public ResponseEntity<User> register(@RequestBody RegisterDto registerDto){
	        User response = authService.registerUser(registerDto);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }
	    
	    
	    @PostMapping("/login")
	    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
	        AuthResponseDto response = authService.login(loginDto);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	    

}
