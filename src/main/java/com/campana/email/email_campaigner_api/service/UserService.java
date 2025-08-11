package com.campana.email.email_campaigner_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	 public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		 this.userRepository = userRepository;
		 this.passwordEncoder = passwordEncoder;
	 }
	
	
	//Metodo para buscar a todos los usuarios
	public List<User> findAllUsers(){
		return userRepository.findAll();
	}
	
	//Metodo para buscar a los usuarios por id
	public Optional<User> findUserById(Long id){
		return Optional.ofNullable(userRepository.findById(id).orElseThrow());
	}
	
	 // Guardar o actualizar un usuario.
    // Para una actualización, el objeto User debe contener el ID.
    public User saveUser(User user) {
        // En caso de actualizar un usuario, si la contraseña ha sido modificada,
        // se debe hashear de nuevo.
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
	
    
    // Eliminar un usuario por su ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
	
}
