package tech.authentication.securityauth.services;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import tech.authentication.securityauth.dto.LoginRequestDto;
import tech.authentication.securityauth.dto.LoginResponseDto;
import tech.authentication.securityauth.entities.Role;
import tech.authentication.securityauth.repositories.UserRepository;

@Service
public class LoginService {

	private final JwtEncoder jwtEncoder;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	
	public LoginService(JwtEncoder jwtEncoder, 
						UserRepository userRepository,
						BCryptPasswordEncoder passwordEncoder) {
		this.jwtEncoder = jwtEncoder; 
		this.userRepository = userRepository; 
		this.passwordEncoder = passwordEncoder;
	}
	
	public LoginResponseDto authenticate(LoginRequestDto loginRequest){
		var user = userRepository.findByEmail(loginRequest.email());
		
		if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
			throw new BadCredentialsException("user or password is invalid!");
		}
		
		var now = Instant.now();
		var expiresIn = 600L; 
		
		var scopes = user.get().getRoles()
				.stream()
				.map(Role::getName)
				.collect(Collectors.joining(" "));
		
		var claims = JwtClaimsSet.builder()
				.issuer("securityAuth")
				.subject(user.get().getUserId().toString())
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiresIn))
				.claim("scope", scopes)
				.build();
				
		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
		return new LoginResponseDto(jwtValue, expiresIn);
	}
}
