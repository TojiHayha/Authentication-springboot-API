package tech.authentication.securityauth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.authentication.securityauth.dto.LoginRequestDto;
import tech.authentication.securityauth.dto.LoginResponseDto;
import tech.authentication.securityauth.services.LoginService;

@RestController
public class LoginController {

	private final LoginService loginService;
	
	
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
		LoginResponseDto response = loginService.authenticate(loginRequestDto);
		return ResponseEntity.ok(response);
	}
	
}
