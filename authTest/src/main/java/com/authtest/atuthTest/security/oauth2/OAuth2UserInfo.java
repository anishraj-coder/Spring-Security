package com.authtest.atuthTest.security.oauth2;

import com.authtest.atuthTest.entities.types.Gender;
import com.authtest.atuthTest.entities.types.Provider;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected final Map<String,Object> attributes;

    public OAuth2UserInfo(Map<String,Object>attributes){this.attributes=attributes;}

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public Gender getGender(){
        Object gender = attributes.get("gender");
        if (gender == null) return Gender.OTHER;
        try {
            return Gender.valueOf(gender.toString().toUpperCase());
        } catch (Exception e) {
            return Gender.OTHER;
        }
    };
    public abstract String getImageUrl();
    public abstract Provider getProvider();

}
