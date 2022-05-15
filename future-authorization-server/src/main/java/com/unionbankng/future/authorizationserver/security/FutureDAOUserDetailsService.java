package com.unionbankng.future.authorizationserver.security;

import com.unionbankng.future.authorizationserver.repositories.UserRepository;
import com.unionbankng.future.authorizationserver.services.UserService;
import com.unionbankng.future.authorizationserver.utils.App;
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
		com.unionbankng.future.authorizationserver.entities.User user = userRepository.findByEmailOrPhoneNumber(username, username).orElseThrow(() -> new BadCredentialsException(
						messageSource.getMessage("username.invalid", null, LocaleContextHolder.getLocale()))
				);
		return new FutureDAOUserDetails(user);
	}

}
