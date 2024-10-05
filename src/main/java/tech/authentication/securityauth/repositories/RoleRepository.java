package tech.authentication.securityauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.authentication.securityauth.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
