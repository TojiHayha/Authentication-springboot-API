package tech.authentication.securityauth.services;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tech.authentication.securityauth.dto.CreateMessageDto;
import tech.authentication.securityauth.dto.FeedDto;
import tech.authentication.securityauth.dto.FeedItemDto;
import tech.authentication.securityauth.entities.Message;
import tech.authentication.securityauth.entities.Role;
import tech.authentication.securityauth.repositories.MessageRepository;
import tech.authentication.securityauth.repositories.UserRepository;

@Service	
public class MessageService {

	private final MessageRepository messageRepository;
	private final UserRepository userRepository;

	public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
	}
	
	public FeedDto getFeed(int page, int pageSize) {
		
		var messages = messageRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
				.map(message -> new FeedItemDto(message.getMessageId(), message.getContent(), message.getUser().getEmail()));
		
		return new FeedDto(messages.getContent(), page, pageSize, messages.getTotalPages(), messages.getTotalElements());
	}
	
	public void createMessage(CreateMessageDto messageDto, JwtAuthenticationToken token) {
		var user = userRepository.findById(UUID.fromString(token.getName()));

		var message = new Message();
		message.setUser(user.get());
		message.setContent(messageDto.content());

		messageRepository.save(message);
	}
	
	public void deleteMessage(Long messageId, JwtAuthenticationToken token) {
		
		var user = userRepository.findById(UUID.fromString(token.getName()));
		var message = messageRepository.findById(messageId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		var isAdmin = user.get().getRoles()
				.stream()
				.anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
		
		if (isAdmin || message.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
			messageRepository.deleteById(messageId);
		} else {
		 throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to delete this message..."); 
		}
	}
	
	
	
}
