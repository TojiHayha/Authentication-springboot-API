package tech.authentication.securityauth.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.transaction.Transactional;
import tech.authentication.securityauth.entities.Role;
import tech.authentication.securityauth.entities.User;
import tech.authentication.securityauth.repositories.RoleRepository;
import tech.authentication.securityauth.repositories.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

	private RoleRepository roleRepository;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	
	public AdminUserConfig(BCryptPasswordEncoder passwordEncoder, 
							UserRepository userRepository, 
							RoleRepository roleRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository; 
	}
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
		var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
	
		var userAdmin = userRepository.findByUsername("admin");
		
		userAdmin.ifPresentOrElse(
				user -> System.out.println("admin ja existe"),
				() -> {
					var user = new User();
					user.setUsername("admin");
					user.setPassword(passwordEncoder.encode("123"));
					user.setRoles(Set.of(roleAdmin));
					userRepository.save(user);
				}
		);
	}
	

}
