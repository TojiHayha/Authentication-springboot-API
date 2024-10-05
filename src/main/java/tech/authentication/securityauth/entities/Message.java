package tech.authentication.securityauth.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "message_id")
	private Long tweetId;
	
	private String content;
	
	@CreationTimestamp
	private Instant creationTimestamp; 
	
	private User user; 
	
	
}
