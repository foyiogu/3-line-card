package com.unionbankng.future.authorizationserver.security;


import java.util.ArrayList;
import java.util.Collection;

import com.unionbankng.future.authorizationserver.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class FutureDAOUserDetails extends User implements UserDetails {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;


	public FutureDAOUserDetails(User user) {
		super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

    	Collection<SimpleGrantedAuthority> c = new ArrayList<>();
    	c.add(new SimpleGrantedAuthority("USER"));
    	return c;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getIsEnabled();
    }
   
    
}