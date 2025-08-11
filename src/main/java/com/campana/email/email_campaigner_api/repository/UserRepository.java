package com.campana.email.email_campaigner_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campana.email.email_campaigner_api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// Método para buscar un usuario por su nombre de usuario
    // El método devuelve un Optional para manejar el caso en que no se encuentre el usuario.
	Optional<User> findByUsername(String username);
	
	
	// Método para buscar un usuario por su dirección de correo electrónico
	Optional<User> findByEmail(String email);
	
	
	//verificar si un usuario con el nombre de usuario dado existe
	boolean existsByUsername(String username);
	
	//verificar si un usuario con el email dado existe
	boolean existsByEmail(String email);
	
	
}
