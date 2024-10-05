package tech.authentication.securityauth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_roles")
public class Role {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "role_id")
	private Long roleId;
	
	private String name;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public enum Values {
		Admin(1L),
		Basic(2L);
		
		long roleId;
		
		Values(long roleId) {
			this.roleId = roleId;
		}

		
		public long getRoleId() {
			return roleId;
		}
		
	}
	
	
	
}
