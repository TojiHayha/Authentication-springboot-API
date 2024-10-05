package tech.authentication.securityauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.authentication.securityauth.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
