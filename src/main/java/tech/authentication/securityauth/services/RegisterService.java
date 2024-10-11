package tech.authentication.securityauth.services;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tech.authentication.securityauth.dto.RegisterUserDto;
import tech.authentication.securityauth.entities.Role;
import tech.authentication.securityauth.entities.User;
import tech.authentication.securityauth.repositories.RoleRepository;
import tech.authentication.securityauth.repositories.UserRepository;

@Service
public class RegisterService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;	
	private final BCryptPasswordEncoder passwordEncoder;
	
	public RegisterService(UserRepository userRepository, 
								RoleRepository roleRepository,
								BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public void registerUser(RegisterUserDto registerDto) {
		
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
	}
	
	public List<User> listUsers(){
		return userRepository.findAll();
	}
}
