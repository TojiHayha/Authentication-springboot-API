package tech.authentication.securityauth.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import tech.authentication.securityauth.dto.CreateMessageDto;
import tech.authentication.securityauth.entities.Message;
import tech.authentication.securityauth.entities.Role;
import tech.authentication.securityauth.repositories.MessageRepository;
import tech.authentication.securityauth.repositories.UserRepository;

@RestController
public class MessageController {

	private final MessageRepository messageRepository;
	private final UserRepository userRepository;

	public MessageController(MessageRepository messageRepository, UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
	}

	@PostMapping("/messages")
	public ResponseEntity<Void> createMessage(@RequestBody CreateMessageDto messageDto, JwtAuthenticationToken token) {
		var user = userRepository.findById(UUID.fromString(token.getName()));

		var message = new Message();
		message.setUser(user.get());
		message.setContent(messageDto.content());

		messageRepository.save(message);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/messages/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long messageId, JwtAuthenticationToken token) {
		
		var user = userRepository.findById(UUID.fromString(token.getName()));
		var message = messageRepository.findById(messageId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		var isAdmin = user.get().getRoles()
				.stream()
				.anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
		
		if (isAdmin || message.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
			messageRepository.deleteById(messageId);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok().build();
	}
}
