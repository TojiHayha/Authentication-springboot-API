package tech.authentication.securityauth.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.authentication.securityauth.dto.RegisterUserDto;
import tech.authentication.securityauth.entities.User;
import tech.authentication.securityauth.services.RegisterService;

@RestController
public class RegisterController {

	private final RegisterService registerService;

	
	public RegisterController(RegisterService registerService) {
			this.registerService = registerService;

	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@RequestBody RegisterUserDto registerDto){
		
		registerService.registerUser(registerDto);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/users")
	@PreAuthorize("hasAuthority('SCOPE_admin')")
	public ResponseEntity<List<User>> listUsers(){
		var users = registerService.listUsers();
		return ResponseEntity.ok(users);
	}
}
