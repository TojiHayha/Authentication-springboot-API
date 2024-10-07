package tech.authentication.securityauth.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import tech.authentication.securityauth.dto.RegisterUserDto;
import tech.authentication.securityauth.entities.Role;
import tech.authentication.securityauth.entities.User;
import tech.authentication.securityauth.repositories.RoleRepository;
import tech.authentication.securityauth.repositories.UserRepository;

@RestController
public class RegisterController {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;	
	private final BCryptPasswordEncoder passwordEncoder;
	
	public RegisterController(UserRepository userRepository, 
								RoleRepository roleRepository,
								BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@RequestBody RegisterUserDto registerDto){
		
		var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
		
		var userFromDb = userRepository.findByUsername(registerDto.username());
		if(userFromDb.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		var user = new User();
		user.setUsername(registerDto.username());
		user.setPassword(passwordEncoder.encode(registerDto.password()));
		user.setRoles(Set.of(basicRole));
		
		userRepository.save(user);
		
		return ResponseEntity.ok().build();
	}
}
