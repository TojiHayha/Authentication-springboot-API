package tech.authentication.securityauth.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.authentication.securityauth.dto.CreateMessageDto;
import tech.authentication.securityauth.entities.Message;
import tech.authentication.securityauth.repositories.MessageRepository;
import tech.authentication.securityauth.repositories.UserRepository;

@RestController
public class MessageController {

	private final MessageRepository messageRepository;
	private final UserRepository userRepository;
	
	public MessageController(MessageRepository messageRepository,
								UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
	}
	
	@PostMapping("/messages")
	public ResponseEntity<Void> createMessage(@RequestBody CreateMessageDto messageDto,
												JwtAuthenticationToken token) {
		var user = userRepository.findById(UUID.fromString(token.getName()));
		
		var message = new Message();
		message.setUser(user.get());
		message.setContent(messageDto.content());
		
		messageRepository.save(message);
		
		return ResponseEntity.ok().build();
	}
}
