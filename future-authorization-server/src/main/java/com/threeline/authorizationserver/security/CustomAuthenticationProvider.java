package com.threeline.authorizationserver.security;

import com.threeline.authorizationserver.pojos.ErrorResponse;
import com.threeline.authorizationserver.utils.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Map;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private  MessageSource messageSource;
    @Autowired
    private App app;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {

        String thirdPartyOauthToken = "";
        if (!(auth.getDetails() instanceof WebAuthenticationDetails)) {
            Map<String, String> map = (Map<String, String>) auth.getDetails();
            thirdPartyOauthToken = map.get("oauth_token");
        }

        String username = auth.getPrincipal() == null ? "NONE_PROVIDED" : auth.getName();
        boolean cacheWasUsed = true;
        FutureDAOUserDetails user = (FutureDAOUserDetails)this.getUserCache().getUserFromCache(username);

        if(user == null) {
            cacheWasUsed =false;

            user = (FutureDAOUserDetails) this.retrieveUser(auth.getName(),
                    (UsernamePasswordAuthenticationToken) auth);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        if(!user.getIsEnabled()){
            errorResponse.setCode("01");
            errorResponse.setRemark(messageSource.getMessage("account.inactive", null, LocaleContextHolder.getLocale()));
            throw new BadCredentialsException(errorResponse.getCode());
        }
        this.getPreAuthenticationChecks().check(user);

        if(user.getPassword()!=null) {
            if(auth.getCredentials() == null && thirdPartyOauthToken != null)
                throw new BadCredentialsException(this.messages.getMessage("wrong.authprovider.email.error", "Bad credentials"));
            additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) auth);
        }


        if (!cacheWasUsed) {
            this.getUserCache().putUserInCache(user);
        }

        this.getPostAuthenticationChecks().check(user);

        Object principalToReturn = user;
        if (this.isForcePrincipalAsString()) {
            principalToReturn = user.getUsername();
        }
        return this.createSuccessAuthentication(principalToReturn, auth, user);
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }

}
