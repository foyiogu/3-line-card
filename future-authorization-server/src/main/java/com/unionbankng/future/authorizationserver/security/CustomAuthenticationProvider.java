package com.unionbankng.future.authorizationserver.security;

import com.unionbankng.future.authorizationserver.entities.Login;
import com.unionbankng.future.authorizationserver.enums.AuthProvider;
import com.unionbankng.future.authorizationserver.interfaceimpl.GoogleOauthProvider;
import com.unionbankng.future.authorizationserver.pojos.ErrorResponse;
import com.unionbankng.future.authorizationserver.repositories.LoginRepository;
import com.unionbankng.future.authorizationserver.services.UserConfirmationTokenService;
import com.unionbankng.future.authorizationserver.utils.App;
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

import java.util.Date;
import java.util.Map;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private GoogleOauthProvider googleOauthProvider;
    @Autowired
    private  MessageSource messageSource;
    @Autowired
    private  UserConfirmationTokenService userConfirmationTokenService;
    @Autowired
    private App app;
    @Autowired
    private  LoginRepository loginRepository;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {

        String thirdPartyOauthToken = "";
        if (!(auth.getDetails() instanceof WebAuthenticationDetails)) {
            Map<String, String> map = (Map<String, String>) auth.getDetails();
            thirdPartyOauthToken = map.get("oauth_token");
        }

//        this.logger.info(String.format("token is : %s",thirdPartyOauthToken));
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
            userConfirmationTokenService.sendConfirmationToken(user);
            errorResponse.setCode("01");
            errorResponse.setRemark(messageSource.getMessage("account.inactive", null, LocaleContextHolder.getLocale()));
            throw new BadCredentialsException(errorResponse.getCode());
        }
        this.getPreAuthenticationChecks().check(user);


        Login loginHistory= new Login();
        loginHistory.setName(user.getFirstName());
        loginHistory.setUsername(user.getUsername());
        loginHistory.setLocation("Lagos/Nigeria");
        loginHistory.setDevice("Not Detected");
        loginHistory.setIpAddress("Not Detected");
        loginHistory.setLoginDate(new Date());
        app.print("########New Login Detected!");

        if(user.getPassword()!=null) {
            if(auth.getCredentials() == null && thirdPartyOauthToken != null)
                throw new BadCredentialsException(this.messages.getMessage("wrong.authprovider.email.error", "Bad credentials"));

            loginHistory.setAuthProvider(AuthProvider.EMAIL);
            loginRepository.save(loginHistory);
            additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) auth);
        }

        if(user.getPassword()==null) {
            if(thirdPartyOauthToken == null)
                throw new BadCredentialsException(this.messages.getMessage("wrong.authprovider.google.error", "Bad credentials"));
            this.logger.info("Using google");
            loginHistory.setAuthProvider(AuthProvider.GOOGLE);
            loginRepository.save(loginHistory);
            googleOauthProvider.authentcate(thirdPartyOauthToken);
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
