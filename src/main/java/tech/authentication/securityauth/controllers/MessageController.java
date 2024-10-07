package tech.authentication.securityauth.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import tech.authentication.securityauth.dto.CreateMessageDto;
import tech.authentication.securityauth.dto.FeedDto;
import tech.authentication.securityauth.dto.FeedItemDto;
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

	@GetMapping("/feed")
	public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
										@RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
		
		var messages = messageRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
				.map(message -> new FeedItemDto(message.getMessageId(), message.getContent(), message.getUser().getUsername()));
		
		return ResponseEntity.ok(new FeedDto(messages.getContent(), page, pageSize, messages.getTotalPages(), messages.getTotalElements()));
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
