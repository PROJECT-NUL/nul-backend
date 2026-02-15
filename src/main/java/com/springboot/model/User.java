package com.springboot.model;

import jakarta.persistence.*;
import lombok.*;
import com.springboot.security.dto.AuthenticatedUserDto;


/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String username;

	private String password;

	private String email;

        @Enumerated(EnumType.STRING)
        private UserRole userRole;

        public AuthenticatedUserDto toAuthenticatedUserDto() {
                AuthenticatedUserDto dto = new AuthenticatedUserDto();
                dto.setName(name);
                dto.setUsername(username);
                dto.setPassword(password);
                dto.setUserRole(userRole);
                return dto;
        }
}
