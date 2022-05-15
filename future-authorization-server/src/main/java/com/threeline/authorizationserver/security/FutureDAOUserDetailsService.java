package com.threeline.authorizationserver.security;

import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.repositories.UserRepository;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FutureDAOUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final MessageSource messageSource;
	private final App app;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmailOrPhoneNumber(username, username).orElseThrow(() -> new BadCredentialsException(
						messageSource.getMessage("username.invalid", null, LocaleContextHolder.getLocale()))
				);
		return new FutureDAOUserDetails(user);
	}

}
