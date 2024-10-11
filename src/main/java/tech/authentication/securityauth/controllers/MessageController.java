package tech.authentication.securityauth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.authentication.securityauth.dto.CreateMessageDto;
import tech.authentication.securityauth.dto.FeedDto;
import tech.authentication.securityauth.services.MessageService;

@RestController
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping("/feed")
	public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
										@RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
		
		return ResponseEntity.ok(messageService.getFeed(page, pageSize));
	}
	
	
	
	@PostMapping("/messages")
	public ResponseEntity<Void> createMessage(@RequestBody CreateMessageDto messageDto, JwtAuthenticationToken token) {
		
		messageService.createMessage(messageDto, token);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/messages/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long messageId, JwtAuthenticationToken token) {
	
		messageService.deleteMessage(messageId, token);
		return ResponseEntity.ok().build();
	}
}
