package com.larabia.springSecurityCourse.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="tbl_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(
			strategy= GenerationType.IDENTITY
			)
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Set<Role> roles;
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//Spring automáticamente espera que los roles empiecen con "ROLE_", por lo tanto automáticamente se le asigna ROLE_ADMIN como autoridad y podrá acceder al endpoint protegido con .hasRole("ADMIN")
	    return roles.stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
	            .toList();
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}
	

}

