package com.springboot.security.service;

import com.springboot.model.User;
import com.springboot.security.dto.AuthenticatedUserDto;
import com.springboot.security.dto.RegistrationRequest;
import com.springboot.security.dto.RegistrationResponse;


/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
public interface UserService {

	User findByUsername(String username);

	RegistrationResponse registration(RegistrationRequest registrationRequest);

	AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

}
