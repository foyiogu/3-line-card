package com.unionbankng.future.authorizationserver.interfaces;
import com.unionbankng.future.authorizationserver.pojos.ThirdPartyOauthResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface ThirdPartyOauthProvider {

    public ThirdPartyOauthResponse authentcate(String idToken);

}
