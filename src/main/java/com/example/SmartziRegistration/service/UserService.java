package com.example.SmartziRegistration.service;



import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.SmartziRegistration.model.User;
import com.example.SmartziRegistration.model.UserRegistrationDto;

public interface UserService extends UserDetailsService {

	 User findByEmail(String email);

	    User save(UserRegistrationDto registration);

	    void updatePassword(String password, Long userId);
}
