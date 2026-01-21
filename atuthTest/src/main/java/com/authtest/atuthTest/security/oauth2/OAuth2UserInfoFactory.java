package com.authtest.atuthTest.security.oauth2;

import com.authtest.atuthTest.entities.types.Provider;
import com.authtest.atuthTest.exception.ProviderNotSupported;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
                                                   Map<String,Object> attributes){
        if(registrationId!=null&&(registrationId.equalsIgnoreCase(Provider.GOOGLE.name())
                ||registrationId.equalsIgnoreCase("google"))){
            return new GoogleOAuth2UserInfo(attributes);
        }else if(registrationId!=null&&(registrationId.equalsIgnoreCase(Provider.GITHUB.name())
                ||registrationId.equalsIgnoreCase("github"))){
            return new GithubOAuth2UserInfo(attributes);
        }else {
            throw new ProviderNotSupported();
        }

    }
}
